package visit

import common.db.RECEIVE_FORMATTER
import common.db.VisitEvent
import common.db.dao.DAO
import common.db.dao.MongoDAO
import common.http.Server
import common.http.queryParameter
import io.reactivex.netty.protocol.http.client.HttpClient
import io.reactivex.netty.protocol.http.server.HttpServerRequest
import rx.Observable

class VisitServer(dao: DAO, private val reporter: HttpClient<*, *>) : Server() {

    private val service = VisitService(dao)

    override fun <T> respond(request: HttpServerRequest<T>): Observable<String> {
        val command = request.decodedPath.substring(1)
        val userId = request.queryParameter<Long>("user_id")
            ?: return Observable.just("Invalid request: 'user_id' not specified or invalid")

        return when (command) {
            "enter" -> VisitService.Commands.Enter.run {
                service
                    .executeCommandWith(userId)
                    .toResponse()
            }
            "exit" -> VisitService.Commands.Exit.run {
                val from = VisitService.Queries.GetLastEvent.run {
                    service.doRequestWith(userId)
                }
                    .toBlocking()
                    .firstOrDefault(null)
                    ?: return@run Observable.just("Illegal state: no previous events found")
                service
                    .executeCommandWith(userId)
                    .toResponse()
                    .also {
                        val until = VisitService.Queries.GetLastEvent.run {
                            service.doRequestWith(userId)
                        }
                            .toBlocking()
                            .firstOrDefault(null)
                            ?: return@also
                        if (
                            from.eventType == VisitEvent.EventType.ENTER &&
                            until.eventType == VisitEvent.EventType.EXIT
                        ) {
                            val fromString = from.timestamp.format(RECEIVE_FORMATTER)
                            val untilString = until.timestamp.format(RECEIVE_FORMATTER)
                            reporter
                                .createGet("/add-info?user_id=$userId&from=$fromString&until=$untilString")
                                .subscribe(System.out::println)
                        }
                    }
            }
            else -> Observable.just("Invalid request: command '$command' not found")
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val reporter = HttpClient
                .newClient("localhost", 8082)
            VisitServer(MongoDAO.DEFAULT, reporter).start(8083)
        }
    }

}
