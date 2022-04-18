package service

import com.mongodb.rx.client.MongoClients
import common.db.MembershipEvent
import common.db.VisitEvent
import common.db.dao.MongoDAO
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import visit.VisitService
import java.time.LocalDateTime

class VisitServiceTest {

    private val dao = MongoDAO(MongoDAO.DEFAULT_URL, "test")
    private val db = MongoClients
        .create(MongoDAO.DEFAULT_URL)
        .getDatabase("test")

    private val service = VisitService(dao)

    @BeforeEach
    fun clean() {
        db.getCollection("visits").drop().toBlocking().single()
        db.getCollection("memberships").drop().toBlocking().single()

        db.getCollection("memberships").insertOne(
            MembershipEvent(
                1,
                1,
                LocalDateTime.now(),
                MembershipEvent.EventType.CREATED,
                LocalDateTime.now().plusMinutes(5)
            ).toDocument()
        ).toBlocking().single()
    }

    @Test
    fun testCanEnter() {
        assertDoesNotThrow {
            VisitService.Commands.Enter.run {
                service.executeCommandWith(1)
                    .toBlocking().single()
            }
            assertEquals(1, dao.visitDAO.getCount().toBlocking().first())
        }
    }

    @Test
    fun testCantReenter() {
        assertThrows<IllegalStateException> {
            dao.visitDAO.addEvent(VisitEvent(1, 1, LocalDateTime.now(), VisitEvent.EventType.ENTER))
                .toBlocking().single()
            VisitService.Commands.Enter.run {
                service.executeCommandWith(1)
                    .toBlocking().single()
            }
        }
    }

    @Test
    fun testCantExit() {
        assertThrows<IllegalStateException> {
            VisitService.Commands.Exit.run {
                service.executeCommandWith(1)
                    .toBlocking().single()
            }
        }
    }

    @Test
    fun testCanExit() {
        assertDoesNotThrow {
            dao.visitDAO.addEvent(VisitEvent(1, 1, LocalDateTime.now(), VisitEvent.EventType.ENTER))
                .toBlocking().single()
            VisitService.Commands.Exit.run {
                service.executeCommandWith(1)
                    .toBlocking().single()
            }
            assertEquals(2, dao.visitDAO.getCount().toBlocking().first())
        }
    }

}
