package dao

import com.mongodb.rx.client.MongoClients
import common.db.MembershipEvent
import common.db.dao.MongoDAO
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class MembershipsDAOTest {

    private val dao = MongoDAO(MongoDAO.DEFAULT_URL, "test")
    private val db = MongoClients
        .create(MongoDAO.DEFAULT_URL)
        .getDatabase("test")

    @BeforeEach
    fun clean() {
        db.getCollection("visits").drop().toBlocking().single()
        db.getCollection("memberships").drop().toBlocking().single()
    }

    @Test
    fun testAddEvent() {
        assertDoesNotThrow {
            dao.membershipDAO.addEvent(
                MembershipEvent(
                    1,
                    1,
                    LocalDateTime.now(),
                    MembershipEvent.EventType.CREATED,
                    LocalDateTime.now()
                )
            ).toBlocking().single()
            assertEquals(1, db.getCollection("memberships").count().toBlocking().first())
        }
    }

    private fun fillData() {
        for (i in 1L..3L) {
            db.getCollection("memberships")
                .insertOne(
                    MembershipEvent(
                        i,
                        i,
                        LocalDateTime.now(),
                        MembershipEvent.EventType.CREATED,
                        LocalDateTime.now()
                    ).toDocument()
                )
                .toBlocking()
                .single()
        }
    }

    @Test
    fun testGetAllEvents() {
        assertDoesNotThrow {
            fillData()
            assertEquals(3, dao.membershipDAO.getAllEvents().count().toBlocking().first())
        }
    }

    @Test
    fun testGetAllEventsForUser() {
        assertDoesNotThrow {
            fillData()
            assertEquals(1, dao.membershipDAO.getAllEventsForUser(1).count().toBlocking().first())
            assertEquals(0, dao.membershipDAO.getAllEventsForUser(4).count().toBlocking().first())
        }
    }

    @Test
    fun testGetCount() {
        assertDoesNotThrow {
            fillData()
            assertEquals(3, dao.membershipDAO.getCount().toBlocking().first())
        }
    }

}
