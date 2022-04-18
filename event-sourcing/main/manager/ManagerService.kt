package manager

import com.mongodb.rx.client.Success
import common.db.MembershipEvent
import common.db.dao.DAO
import common.service.Command
import common.service.Query
import common.service.Service
import rx.Observable
import java.time.LocalDateTime

class ManagerService(private val dao: DAO) : Service {

    object Commands {
        data class Payload(val userId: Long, val until: LocalDateTime)

        object Create : Command<ManagerService, Payload> {
            override fun ManagerService.executeCommandWith(data: Payload): Observable<Success> {
                return createMembership(data.userId, data.until)
            }
        }

        object Extend : Command<ManagerService, Payload> {
            override fun ManagerService.executeCommandWith(data: Payload): Observable<Success> {
                return extendMembership(data.userId, data.until)
            }
        }
    }

    object Queries {
        object GetLastEvent : Query<ManagerService, Long, MembershipEvent> {
            override fun ManagerService.doRequestWith(data: Long): Observable<MembershipEvent> {
                return lastEvent(data)
            }
        }
    }

    private fun lastEvent(userId: Long): Observable<MembershipEvent> {
        return dao.membershipDAO
            .getAllEventsForUser(userId)
            .firstOrDefault(null)
    }

    private fun addEvent(
        userId: Long,
        timestamp: LocalDateTime,
        eventType: MembershipEvent.EventType,
        until: LocalDateTime
    ): Observable<Success> {
        return dao.membershipDAO
            .getCount()
            .first()
            .flatMap {
                val event = MembershipEvent(it, userId, timestamp, eventType, until)
                dao.membershipDAO.addEvent(event)
            }
    }

    private fun createMembership(userId: Long, until: LocalDateTime): Observable<Success> {
        val now = LocalDateTime.now()
        if (until.isBefore(now)) {
            return Observable.error(IllegalArgumentException("Can't create a membership until past date"))
        }
        return lastEvent(userId)
            .flatMap { last ->
                when {
                    last == null || last.until.isBefore(now) ->
                        addEvent(userId, now, MembershipEvent.EventType.CREATED, until)
                    else ->
                        Observable.error(IllegalStateException("User already has active membership"))
                }
            }
    }

    private fun extendMembership(userId: Long, until: LocalDateTime): Observable<Success> {
        val now = LocalDateTime.now()
        if (until.isBefore(now)) {
            return Observable.error(IllegalArgumentException("Can't extend a membership to past date"))
        }
        return lastEvent(userId)
            .flatMap { last ->
                when {
                    last == null || last.until.isBefore(now) ->
                        Observable.error(IllegalStateException("No active membership"))
                    until.isBefore(last.until) ->
                        Observable.error(IllegalArgumentException("Can't shorten a membership"))
                    else ->
                        addEvent(userId, now, MembershipEvent.EventType.EXTENDED, until)
                }
            }
    }

}
