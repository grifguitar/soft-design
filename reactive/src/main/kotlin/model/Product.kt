package model

import org.bson.Document

class Product(
    private val id: String?,
    private val name: String,
    private val price: Double
) : Entity {

    constructor(document: Document) : this(
        document["_id"] as String,
        document["name"] as String,
        document["price"] as Double
    )

    override fun toDocument(): Document {
        val document = Document()
        document.append("_id", id)
        document.append("name", name)
        document.append("price", price)
        return document
    }

    fun convert(currency: Currency): String {
        return "<body><h3>" +
                "<br>Product {" +
                "<br>\"id\": <font color=blue>$id</font>," +
                "<br>\"name\": <font color=blue>$name</font>," +
                "<br>\"price\": <font color=blue>${Currency.convert(Currency.RUB, currency, price)}</font>" +
                "<br>}" +
                "</h3></body>"
    }

    override fun toString(): String {
        return convert(Currency.RUB)
    }
}
