package devpub.blogengine.util

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

object DateTimeUtils {
    private val defaultZoneId = ZoneId.systemDefault()
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    fun toLocalDate(date: Date): LocalDate {
        return Instant.ofEpochMilli(date.time)
                      .atZone(defaultZoneId)
                      .toLocalDate();
    }

    fun format(date: LocalDate): String {
        return dateFormatter.format(date)
    }

    fun format(dateTime: LocalDateTime): String {
        return dateTimeFormatter.format(dateTime)
    }
}