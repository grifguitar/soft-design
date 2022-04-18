package manager

import common.db.dao.DAO
import common.db.dao.MongoDAO
import common.http.Server
import common.http.queryParameter
import io.reactivex.netty.protocol.http.server.HttpServerRequest
import rx.Observable
import java.time.LocalDateTime

class ManagerServer(dao: DAO) : Server() {

    private val service = ManagerService(dao)

    override fun <T> respond(request: HttpServerRequest<T>): Observable<String> {
        val command = request.decodedPath.substring(1)
        val userId = request.queryParameter<Long>("user_id")
            ?: return Observable.just("Invalid request: 'user_id' not specified or invalid")
        val until = request.queryParameter<LocalDateTime>("until")
            ?: return Observable.just("Invalid request: 'until' not specified or invalid")

        val payload = ManagerService.Commands.Payload(userId, until)

        return when (command) {
            "create" -> ManagerService.Commands.Create.run {
                service
                    .executeCommandWith(payload)
                    .toResponse()
            }
            "extend" -> ManagerService.Commands.Extend.run {
                service
                    .executeCommandWith(payload)
                    .toResponse()
            }
            "get-info" -> ManagerService.Queries.GetLastEvent.run {
                service
                    .doRequestWith(userId)
                    .flatMap { Observable.just(it?.toString() ?: "None") }
            }
            else -> Observable.just("Invalid request: command '$command' not found")
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            ManagerServer(MongoDAO.DEFAULT).start(8081)
        }
    }

}
