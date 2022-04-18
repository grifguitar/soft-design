package common.db.dao.module

import com.mongodb.rx.client.Success
import common.db.Event
import rx.Observable

interface ModuleDAO<E : Event> {

    fun addEvent(event: E): Observable<Success>

    fun getAllEventsForUser(userId: Long): Observable<E>

    fun getAllEvents(): Observable<E>

    fun getCount(): Observable<Long>

}
