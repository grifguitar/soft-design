package service

import com.mongodb.rx.client.MongoClients
import common.db.MembershipEvent
import common.db.VisitEvent
import common.db.dao.MongoDAO
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import report.ReportService
import java.time.Duration
import java.time.LocalDateTime

class ReportServiceTest {

    private val dao = MongoDAO(MongoDAO.DEFAULT_URL, "test")
    private val db = MongoClients
        .create(MongoDAO.DEFAULT_URL)
        .getDatabase("test")

    private var service: ReportService? = null

    @BeforeEach
    fun clean() {
        db.getCollection("visits").drop().toBlocking().single()
        db.getCollection("memberships").drop().toBlocking().single()

        db.getCollection("memberships").insertOne(
            MembershipEvent(
                1,
                1,
                LocalDateTime.now().minusDays(1).minusMinutes(20),
                MembershipEvent.EventType.CREATED,
                LocalDateTime.now().plusMinutes(30)
            ).toDocument()
        ).toBlocking().single()
        db.getCollection("visits").insertOne(
            VisitEvent(
                1, 1, LocalDateTime.now().minusDays(1), VisitEvent.EventType.ENTER
            ).toDocument()
        ).toBlocking().single()
        db.getCollection("visits").insertOne(
            VisitEvent(
                1, 1, LocalDateTime.now().minusDays(1).plusMinutes(20), VisitEvent.EventType.EXIT
            ).toDocument()
        ).toBlocking().single()

        service = ReportService(dao)
        Thread.sleep(100)
    }

    @Test
    fun testFetches() {
        assertDoesNotThrow {
            assertEquals(1, ReportService.Queries.GetTotalReport.run {
                service!!.doRequestWith(1)
            }.toBlocking().first().totalCount)
        }
    }

    @Test
    fun testRespondsToUpdates() {
        assertDoesNotThrow {
            ReportService.Commands.AddVisitInfo.run {
                service!!.executeCommandWith(
                    ReportService.Commands.Payload(
                        1, LocalDateTime.now().minusMinutes(10), LocalDateTime.now()
                    )
                )
            }.toBlocking().single()
            assertEquals(2, ReportService.Queries.GetTotalReport.run {
                service!!.doRequestWith(1)
            }.toBlocking().first().totalCount)
        }
    }

    @Test
    fun testReportIsCorrect() {
        assertDoesNotThrow {
            val data = ReportService.Queries.GetDailyReport.run {
                service!!.doRequestWith(1)
            }.toBlocking().first().data
            assertEquals(1, data.size)
            assertEquals(Duration.ofMinutes(20), data.values.first())
        }
    }

}
