package common.db.dao.module

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts
import com.mongodb.rx.client.MongoCollection
import com.mongodb.rx.client.Success
import common.db.Event
import org.bson.Document
import rx.Observable

abstract class MongoModuleDAO<E : Event>(
    private val collection: MongoCollection<Document>
) : ModuleDAO<E> {

    protected abstract fun mapper(document: Document): E

    override fun addEvent(event: E): Observable<Success> {
        return collection.insertOne(event.toDocument())
    }

    override fun getAllEventsForUser(userId: Long): Observable<E> {
        return getAllDocumentsForUser(userId).map(::mapper)
    }

    override fun getAllEvents(): Observable<E> {
        return getAllDocuments().map(::mapper)
    }

    override fun getCount(): Observable<Long> {
        return collection.count()
    }

    private fun getAllDocumentsForUser(userId: Long): Observable<Document> {
        return collection
            .find(Filters.eq("user_id", userId))
            .sort(Sorts.descending("timestamp"))
            .toObservable()
    }

    private fun getAllDocuments(): Observable<Document> {
        return collection
            .find()
            .toObservable()
    }

}
