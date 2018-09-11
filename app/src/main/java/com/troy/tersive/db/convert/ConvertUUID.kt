package com.troy.tersive.db.convert

import androidx.room.TypeConverter
import java.util.UUID

class ConvertUUID {

    @TypeConverter
    fun fromUUID(value: UUID?): String? {
        return value?.toString()
    }

    @TypeConverter
    fun toUUID(value: String?): UUID? {
        return if (value == null) null else UUID.fromString(value)
    }
}