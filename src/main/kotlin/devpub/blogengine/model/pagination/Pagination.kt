package devpub.blogengine.model.pagination

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import java.io.Serializable
import kotlin.math.max

data class Pagination(
    private val offset: Long,
    private val limit: Int,
    private val sort: Sort
): Pageable, Serializable {
    constructor(offset: Long, limit: Int): this(offset, limit, Sort.unsorted())

    override fun getOffset(): Long {
        return offset
    }

    override fun getPageSize(): Int {
        return limit
    }

    override fun getPageNumber(): Int {
        return (offset / limit).toInt()
    }

    override fun getSort(): Sort {
        return sort
    }

    override fun hasPrevious(): Boolean {
        return offset >= limit
    }

    override fun first(): Pagination {
        return Pagination(0, limit, sort)
    }

    override fun next(): Pagination {
        return Pagination(offset + limit, limit, sort)
    }

    override fun previousOrFirst(): Pagination {
        return Pagination(max(0, offset - limit), limit, sort)
    }
}