package devpub.blogengine.model

import com.fasterxml.jackson.annotation.JsonProperty
import devpub.blogengine.model.entity.projection.PostCountToDate
import devpub.blogengine.util.DateTimeUtils
import java.util.Objects
import java.util.TreeSet

class PostCountToDatesResponse private constructor(
    val years: Set<Int>,
    @JsonProperty("posts") val countToDates: Map<String, Int>
) {
    companion object {
        fun of(countToDateList: Collection<PostCountToDate>): PostCountToDatesResponse {
            if(countToDateList.isEmpty()) {
                PostCountToDatesResponse(emptySet(), emptyMap())
            }

            val years = TreeSet<Int> { a, b -> b.compareTo(a) }
            val countToDates = hashMapOf<String, Int>()

            countToDateList.forEach {
                val formattedDate = DateTimeUtils.format(it.date)
                countToDates[formattedDate] = (countToDates[formattedDate] ?: 0) + it.count
                years.add(it.date.year)
            }

            return PostCountToDatesResponse(years, countToDates)
        }
    }

    override fun hashCode(): Int {
        return Objects.hash(years, countToDates)
    }

    override fun equals(other: Any?): Boolean {
        return other is PostCountToDatesResponse && years == other.years && countToDates == other.countToDates
    }
}