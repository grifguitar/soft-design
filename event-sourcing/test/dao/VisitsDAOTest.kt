package dao

import com.mongodb.rx.client.MongoClients
import common.db.VisitEvent
import common.db.dao.MongoDAO
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class VisitsDAOTest {

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
            dao.visitDAO.addEvent(VisitEvent(1, 1, LocalDateTime.now(), VisitEvent.EventType.ENTER))
                .toBlocking().single()
            assertEquals(1, db.getCollection("visits").count().toBlocking().first())
        }
    }

    private fun fillData() {
        for (i in 1L..3L) {
            db.getCollection("visits")
                .insertOne(VisitEvent(i, i, LocalDateTime.now(), VisitEvent.EventType.ENTER).toDocument())
                .toBlocking()
                .single()
        }
    }

    @Test
    fun testGetAllEvents() {
        assertDoesNotThrow {
            fillData()
            assertEquals(3, dao.visitDAO.getAllEvents().count().toBlocking().first())
        }
    }

    @Test
    fun testGetAllEventsForUser() {
        assertDoesNotThrow {
            fillData()
            assertEquals(1, dao.visitDAO.getAllEventsForUser(1).count().toBlocking().first())
            assertEquals(0, dao.visitDAO.getAllEventsForUser(4).count().toBlocking().first())
        }
    }

    @Test
    fun testGetCount() {
        assertDoesNotThrow {
            fillData()
            assertEquals(3, dao.visitDAO.getCount().toBlocking().first())
        }
    }

}
