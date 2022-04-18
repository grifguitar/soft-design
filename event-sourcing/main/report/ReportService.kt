package report

import com.mongodb.rx.client.Success
import common.db.VisitEvent
import common.db.dao.DAO
import common.service.Command
import common.service.Query
import common.service.Service
import rx.Observable
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ReportService(dao: DAO) : Service {

    data class VisitInfo(
        val from: LocalDateTime,
        val until: LocalDateTime
    ) {
        val length: Duration = Duration.between(from, until)
    }

    data class DailyReport(
        val data: Map<String, Duration>
    )

    data class TotalReport(
        val totalCount: Int,
        val activeDays: Long,
        val totalDuration: Duration
    )

    private val statistics: MutableMap<Long, MutableList<VisitInfo>> = HashMap()

    companion object {
        private val REPORT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    }

    object Commands {
        data class Payload(
            val userId: Long,
            val from: LocalDateTime,
            val until: LocalDateTime
        )

        object AddVisitInfo : Command<ReportService, Payload> {
            override fun ReportService.executeCommandWith(data: Payload): Observable<Success> {
                return addVisitInfo(data.userId, data.from, data.until)
            }
        }
    }

    object Queries {
        object GetDailyReport : Query<ReportService, Long, DailyReport> {
            override fun ReportService.doRequestWith(data: Long): Observable<DailyReport> {
                return getDailyUserReport(data)
            }
        }

        object GetTotalReport : Query<ReportService, Long, TotalReport> {
            override fun ReportService.doRequestWith(data: Long): Observable<TotalReport> {
                return getTotalUserReport(data)
            }
        }
    }

    init {
        dao.visitDAO
            .getAllEvents()
            .groupBy { it.userId }
            .forEach { group ->
                statistics[group.key] = ArrayList()
                var lastEvent: VisitEvent? = null
                group.forEach { event ->
                    lastEvent = if (lastEvent?.eventType == VisitEvent.EventType.ENTER) {
                        statistics[group.key]!!.add(VisitInfo(lastEvent!!.timestamp, event.timestamp))
                        null
                    } else {
                        event
                    }
                }
            }
    }

    private fun addVisitInfo(userId: Long, from: LocalDateTime, until: LocalDateTime): Observable<Success> {
        if (from > until) {
            return Observable.error(IllegalArgumentException("Invalid interval bounds"))
        }
        if (until > LocalDateTime.now()) {
            return Observable.error(IllegalArgumentException("Can't end the visit in the future"))
        }
        statistics.getOrPut(userId) { ArrayList() }.add(VisitInfo(from, until))
        return Observable.just(Success.SUCCESS)
    }

    private fun getDailyUserReport(userId: Long): Observable<DailyReport> {
        return Observable.just(DailyReport(
            statistics.getOrElse(userId) { ArrayList() }
                .groupBy { it.from.format(REPORT_FORMATTER) }
                .mapValues { Duration.ofMillis(it.value.map { visit -> visit.length.toMillis() }.sum()) }
        ))
    }

    private fun getTotalUserReport(userId: Long): Observable<TotalReport> {
        val data = statistics.getOrElse(userId) { ArrayList() }
        val days = if (data.isEmpty()) 0 else Duration.between(data.first().from, data.last().from).toDays() + 1
        return Observable.just(
            TotalReport(
                data.size,
                days,
                Duration.ofMillis(data.map { visit -> visit.length.toMillis() }.sum())
            )
        )
    }

}
