package visit

import com.mongodb.rx.client.Success
import common.db.VisitEvent
import common.db.dao.DAO
import common.service.Command
import common.service.Query
import common.service.Service
import rx.Observable
import java.time.LocalDateTime

class VisitService(private val dao: DAO) : Service {

    object Commands {
        object Enter : Command<VisitService, Long> {
            override fun VisitService.executeCommandWith(data: Long): Observable<Success> {
                return registerEnter(data)
            }
        }

        object Exit : Command<VisitService, Long> {
            override fun VisitService.executeCommandWith(data: Long): Observable<Success> {
                return registerExit(data)
            }
        }
    }

    object Queries {
        object GetLastEvent : Query<VisitService, Long, VisitEvent> {
            override fun VisitService.doRequestWith(data: Long): Observable<VisitEvent> {
                return lastEvent(data)
            }
        }
    }

    private fun lastEvent(userId: Long): Observable<VisitEvent> {
        return dao.visitDAO
            .getAllEventsForUser(userId)
            .firstOrDefault(null)
    }

    private fun addEvent(userId: Long, timestamp: LocalDateTime, eventType: VisitEvent.EventType): Observable<Success> {
        return dao.visitDAO
            .getCount()
            .first()
            .flatMap {
                val event = VisitEvent(it, userId, timestamp, eventType)
                dao.visitDAO.addEvent(event)
            }
    }

    private fun registerEnter(userId: Long): Observable<Success> {
        return dao.membershipDAO
            .getAllEventsForUser(userId)
            .firstOrDefault(null)
            .flatMap { membership ->
                val now = LocalDateTime.now()
                when {
                    membership == null ->
                        Observable.error(IllegalStateException("No membership found"))
                    membership.until.isAfter(now) ->
                        lastEvent(userId)
                            .flatMap { last ->
                                when {
                                    last == null || last.eventType == VisitEvent.EventType.EXIT ->
                                        addEvent(userId, now, VisitEvent.EventType.ENTER)
                                    else ->
                                        Observable.error(IllegalStateException("Re-enter attempt"))
                                }
                            }
                    else ->
                        Observable.error(IllegalStateException("All memberships are expired"))
                }
            }
    }

    private fun registerExit(userId: Long): Observable<Success> {
        return lastEvent(userId)
            .flatMap { last ->
                when {
                    last == null || last.eventType == VisitEvent.EventType.EXIT ->
                        Observable.error(IllegalStateException("No pre-entrance event"))
                    else ->
                        addEvent(userId, LocalDateTime.now(), VisitEvent.EventType.EXIT)
                }
            }
    }

}
