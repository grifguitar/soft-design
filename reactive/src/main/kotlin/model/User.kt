package model

import org.bson.Document

class User(
    private val id: String?,
    private val name: String,
    val currency: Currency
) : Entity {

    constructor(document: Document) : this(
        document["_id"] as String,
        document["name"] as String,
        Currency.valueOf(document["currency"] as String)
    )

    override fun toDocument(): Document {
        val document = Document()
        document.append("_id", id)
        document.append("name", name)
        document.append("currency", currency.fullName)
        return document
    }

    override fun toString(): String {
        return "<body><h3>" +
                "<br>User {" +
                "<br>\"id\": <font color=blue>$id</font>," +
                "<br>\"name\": <font color=blue>$name</font>," +
                "<br>\"currency\": <font color=blue>${currency.fullName}</font>" +
                "<br>}" +
                "</h3></body>"
    }
}
