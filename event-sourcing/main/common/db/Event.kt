package common.db

import org.bson.Document
import java.time.LocalDateTime

sealed class Event(
    val id: Long,
    val userId: Long,
    val timestamp: LocalDateTime
) {

    open fun toDocument(): Document = Document()
        .append("id", id)
        .append("user_id", userId)
        .append("timestamp", timestamp.format(FORMATTER))

}

class VisitEvent(
    id: Long,
    userId: Long,
    timestamp: LocalDateTime,
    val eventType: EventType
) : Event(id, userId, timestamp) {

    enum class EventType {
        ENTER,
        EXIT
    }

    constructor(document: Document) : this(
        document.getLong("id"),
        document.getLong("user_id"),
        LocalDateTime.parse(document.getString("timestamp"), FORMATTER),
        EventType.valueOf(document.getString("event_type"))
    )

    override fun toDocument(): Document = super.toDocument()
        .append("event_type", eventType.toString())

}

class MembershipEvent(
    id: Long,
    userId: Long,
    timestamp: LocalDateTime,
    val eventType: EventType,
    val until: LocalDateTime
) : Event(id, userId, timestamp) {

    enum class EventType {
        CREATED,
        EXTENDED
    }

    constructor(document: Document) : this(
        document.getLong("id"),
        document.getLong("user_id"),
        LocalDateTime.parse(document.getString("timestamp"), FORMATTER),
        EventType.valueOf(document.getString("event_type")),
        LocalDateTime.parse(document.getString("until"), FORMATTER)
    )

    override fun toDocument(): Document = super.toDocument()
        .append("event_type", eventType.toString())
        .append("until", until.format(FORMATTER))

}
