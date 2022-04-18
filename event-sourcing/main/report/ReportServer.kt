package report

import common.db.dao.DAO
import common.db.dao.MongoDAO
import common.http.Server
import common.http.queryParameter
import io.reactivex.netty.protocol.http.server.HttpServerRequest
import rx.Observable
import java.time.LocalDateTime

class ReportServer(dao: DAO) : Server() {

    private val service = ReportService(dao)

    override fun <T> respond(request: HttpServerRequest<T>): Observable<String> {
        val command = request.decodedPath.substring(1)
        val userId = request.queryParameter<Long>("user_id")
            ?: return Observable.just("Invalid request: 'user_id' not specified or invalid")

        return when (command) {
            "add-info" -> ReportService.Commands.AddVisitInfo.run {
                val from = request.queryParameter<LocalDateTime>("from")
                    ?: return@run Observable.just("Invalid request: 'from' not specified or invalid")
                val until = request.queryParameter<LocalDateTime>("until")
                    ?: return@run Observable.just("Invalid request: 'until' not specified or invalid")
                val payload = ReportService.Commands.Payload(userId, from, until)
                service
                    .executeCommandWith(payload)
                    .toResponse()
            }
            "daily-report" -> ReportService.Queries.GetDailyReport.run {
                service.doRequestWith(userId)
                    .map { it.toString() }
            }
            "total-report" -> ReportService.Queries.GetTotalReport.run {
                service.doRequestWith(userId)
                    .map { it.toString() }
            }
            else -> Observable.just("Invalid request: command '$command' not found")
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            ReportServer(MongoDAO.DEFAULT).start(8082)
        }
    }

}
