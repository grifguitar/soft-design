package model

import org.bson.Document

interface Entity {
    fun toDocument(): Document
}
