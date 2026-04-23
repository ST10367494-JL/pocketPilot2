package com.pocketpilot.pocketpilot.data

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.util.Date



public class PocketTypeConverters {

    @TypeConverter
    public fun fromTimestamp(value: String?): ZonedDateTime? {
        // BEGIN-CITATIONS java-isodate-format
        // LINK https://docs.oracle.com/javase/8/docs/api/java/time/ZonedDateTime.html#parse-java.lang.CharSequence-
        // DESC Use ZonedDateTime to ensure we get the most accurate date and time serialisation
        // ACCESSED 20260422T131338.511643845+0200
        // CSL-REF oracle-java-javadoc-time-zoneddatetime
        // END-CITATIONS
        return if (value == null) null else ZonedDateTime.parse(value)
    }

    @TypeConverter
    public fun dateToTimestamp(date: ZonedDateTime?): String? {
        return date?.toString()
    }
}