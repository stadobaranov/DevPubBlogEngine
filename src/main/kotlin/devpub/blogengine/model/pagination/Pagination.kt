package devpub.blogengine.model.pagination

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import java.io.Serializable
import java.util.Objects
import kotlin.math.max

open class Pagination(
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

    override fun first(): Pageable {
        return Pagination(0, limit, sort)
    }

    override fun next(): Pageable {
        return Pagination(offset + limit, limit, sort)
    }

    override fun previousOrFirst(): Pageable {
        return Pagination(max(0, offset - limit), limit, sort)
    }

    override fun hashCode(): Int {
        return Objects.hash(offset, limit, sort)
    }

    override fun equals(other: Any?): Boolean {
        return other is Pagination && offset == other.offset && limit == other.limit && sort == other.sort
    }
}