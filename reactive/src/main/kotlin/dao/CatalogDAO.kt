package dao

import com.mongodb.rx.client.MongoClient
import com.mongodb.rx.client.MongoClients
import com.mongodb.rx.client.MongoCollection
import com.mongodb.rx.client.MongoDatabase
import com.mongodb.rx.client.Success
import model.Product
import model.User
import org.bson.Document
import rx.Observable

object CatalogDAO {
    private const val PORT = 27017
    private val mongoClient: MongoClient = MongoClients.create("mongodb://localhost:$PORT")
    private val mongoDatabase: MongoDatabase = mongoClient.getDatabase("catalog")

    private fun users(): MongoCollection<Document> {
        return mongoDatabase.getCollection("users")
    }

    private fun products(): MongoCollection<Document> {
        return mongoDatabase.getCollection("products")
    }

    fun addUser(user: Document): Observable<Success> {
        return users().insertOne(user)
    }

    fun getUser(id: String): Observable<User> {
        return users().find().toObservable().filter { it["_id"] == (id) }.map { User(it) }
    }

    fun addProduct(product: Document): Observable<Success> {
        return products().insertOne(product)
    }

    fun getAllProducts(): Observable<Product> {
        return products().find().toObservable().map { Product(it) }
    }
}
