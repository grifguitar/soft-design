package common.http

import common.db.RECEIVE_FORMATTER
import io.reactivex.netty.protocol.http.server.HttpServerRequest
import java.time.LocalDateTime

inline fun <reified R> HttpServerRequest<*>.queryParameter(name: String): R? {
    val values = queryParameters[name] ?: return null
    if (values.isEmpty()) {
        return null
    }

    val value = values[0] ?: return null
    return when (R::class.qualifiedName) {
        "kotlin.String" -> value as R
        "kotlin.Int" -> value.toInt() as R
        "kotlin.Long" -> value.toLong() as R
        "java.time.LocalDateTime" -> LocalDateTime.parse(value, RECEIVE_FORMATTER) as R
        else -> throw IllegalArgumentException("Can't parse argument '$value' as '${R::class.qualifiedName}'")
    }
}
