package com.pocketpilot.pocketpilot.data

import androidx.room.TypeConverter
import java.util.Date

class PocketTypeConverters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time
}
