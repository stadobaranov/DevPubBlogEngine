package devpub.blogengine.service

import java.time.LocalDateTime

interface PrettyTimeFormatter {
    fun format(time: LocalDateTime): String
}