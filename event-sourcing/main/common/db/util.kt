package common.db

import java.time.format.DateTimeFormatter

val FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
val RECEIVE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm")
