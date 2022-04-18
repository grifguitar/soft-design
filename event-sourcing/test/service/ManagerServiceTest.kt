package service

import com.mongodb.rx.client.MongoClients
import common.db.MembershipEvent
import common.db.dao.MongoDAO
import manager.ManagerService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime

class ManagerServiceTest {

    private val dao = MongoDAO(MongoDAO.DEFAULT_URL, "test")
    private val db = MongoClients
        .create(MongoDAO.DEFAULT_URL)
        .getDatabase("test")

    private val service = ManagerService(dao)

    @BeforeEach
    fun clean() {
        db.getCollection("visits").drop().toBlocking().single()
        db.getCollection("memberships").drop().toBlocking().single()
    }

    private fun fillData() {
        dao.membershipDAO.addEvent(
            MembershipEvent(
                1,
                1,
                LocalDateTime.now(),
                MembershipEvent.EventType.CREATED,
                LocalDateTime.now().plusMinutes(5)
            )
        ).toBlocking().single()
    }

    @Test
    fun testCreate() {
        assertDoesNotThrow {
            ManagerService.Commands.Create.run {
                service
                    .executeCommandWith(ManagerService.Commands.Payload(1, LocalDateTime.now().plusMinutes(1)))
                    .toBlocking().single()
            }
            assertEquals(1, dao.membershipDAO.getCount().toBlocking().first())
        }
    }

    @Test
    fun testCreateAfterExpire() {
        assertDoesNotThrow {
            dao.membershipDAO.addEvent(
                MembershipEvent(
                    1,
                    1,
                    LocalDateTime.now().minusMinutes(10),
                    MembershipEvent.EventType.CREATED,
                    LocalDateTime.now().minusMinutes(5)
                )
            ).toBlocking().single()
            ManagerService.Commands.Create.run {
                service
                    .executeCommandWith(ManagerService.Commands.Payload(1, LocalDateTime.now().plusMinutes(1)))
                    .toBlocking().single()
            }
            assertEquals(2, dao.membershipDAO.getCount().toBlocking().first())
        }
    }

    @Test
    fun testCantCreateAnother() {
        assertThrows<IllegalStateException> {
            fillData()
            ManagerService.Commands.Create.run {
                service
                    .executeCommandWith(ManagerService.Commands.Payload(1, LocalDateTime.now().plusMinutes(1)))
                    .toBlocking().single()
            }
            assertEquals(1, dao.membershipDAO.getCount().toBlocking().first())
        }
    }

    @Test
    fun testCantShorten() {
        assertThrows<IllegalArgumentException> {
            fillData()
            val lowerNow = LocalDateTime.now().plusMinutes(1)
            ManagerService.Commands.Extend.run {
                service
                    .executeCommandWith(ManagerService.Commands.Payload(1, lowerNow))
                    .toBlocking().single()
            }
            assertNotEquals(lowerNow, dao.membershipDAO.getAllEvents().toBlocking().first().until)
        }
    }

    @Test
    fun testCanExtend() {
        assertDoesNotThrow {
            fillData()
            val higherNow = LocalDateTime.now().plusMinutes(10)
            ManagerService.Commands.Extend.run {
                service
                    .executeCommandWith(ManagerService.Commands.Payload(1, higherNow))
                    .toBlocking().single()
            }
            assertEquals(2, dao.membershipDAO.getCount().toBlocking().first())
        }
    }

}
