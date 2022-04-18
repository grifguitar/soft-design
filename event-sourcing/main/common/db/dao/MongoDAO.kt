package common.db.dao

import com.mongodb.rx.client.MongoClients
import com.mongodb.rx.client.MongoDatabase
import common.db.MembershipEvent
import common.db.VisitEvent
import common.db.dao.module.ModuleDAO
import common.db.dao.module.MongoModuleDAO
import org.bson.Document

class MongoDAO(
    connectionURL: String,
    dbName: String
) : DAO {

    companion object {
        const val DEFAULT_URL = "mongodb://localhost:27017"
        const val DEFAULT_DB_NAME = "fitness-center"

        val DEFAULT = MongoDAO(DEFAULT_URL, DEFAULT_DB_NAME)
    }

    private val db: MongoDatabase = MongoClients
        .create(connectionURL)
        .getDatabase(dbName)

    private val visits = db.getCollection("visits")
    private val memberships = db.getCollection("memberships")

    inner class VisitDAO : MongoModuleDAO<VisitEvent>(visits) {
        override fun mapper(document: Document): VisitEvent {
            return VisitEvent(document)
        }
    }

    inner class MembershipDAO : MongoModuleDAO<MembershipEvent>(memberships) {
        override fun mapper(document: Document): MembershipEvent {
            return MembershipEvent(document)
        }
    }

    override val visitDAO: ModuleDAO<VisitEvent> = VisitDAO()
    override val membershipDAO: ModuleDAO<MembershipEvent> = MembershipDAO()

}
