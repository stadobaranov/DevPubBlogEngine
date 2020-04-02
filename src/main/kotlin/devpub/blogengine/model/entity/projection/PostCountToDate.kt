package devpub.blogengine.model.entity.projection

import devpub.blogengine.util.DateTimeUtils
import java.time.LocalDate
import java.util.Date

data class PostCountToDate(val date: LocalDate, val count: Int) {
    @Deprecated("Конструктор для JPA")
    constructor(date: Date, count: Long): this(DateTimeUtils.toLocalDate(date), count.toInt())
}