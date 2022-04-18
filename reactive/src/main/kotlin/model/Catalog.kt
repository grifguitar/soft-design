package model

import dao.CatalogDAO
import org.bson.types.ObjectId
import rx.Observable

object Catalog {
    fun addUser(name: String, currency: String): Observable<String> {
        val id = ObjectId().toString()
        return CatalogDAO.addUser(User(id, name, Currency.valueOf(currency)).toDocument()).map { id }
    }

    fun getUser(id: String): Observable<String> {
        return CatalogDAO.getUser(id).map { it.toString() }
    }

    fun addProduct(name: String, price: Double): Observable<String> {
        val id = ObjectId().toString()
        return CatalogDAO.addProduct(Product(id, name, price).toDocument()).map { id }
    }

    fun getAllProducts(userId: String): Observable<String> {
        return CatalogDAO.getUser(userId)
            .flatMap { user -> CatalogDAO.getAllProducts().map { it.convert(user.currency) } }
    }
}
