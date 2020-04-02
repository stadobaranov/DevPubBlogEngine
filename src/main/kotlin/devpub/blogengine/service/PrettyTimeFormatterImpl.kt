package devpub.blogengine.service

import org.ocpsoft.prettytime.PrettyTime
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

private val defaultZoneId = ZoneId.systemDefault()

@Service
open class PrettyTimeFormatterImpl: PrettyTimeFormatter {
    private val prettyTime = PrettyTime()

    override fun format(time: LocalDateTime): String {
        return prettyTime.format(Date.from(time.atZone(defaultZoneId).toInstant()))
    }
}