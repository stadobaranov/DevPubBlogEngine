package devpub.blogengine.model.pagination

import kotlin.math.max

data class UnorderedPagination(
    private val offset: Long,
    private val limit: Int
): UnorderedPageable() {
    override fun getOffset(): Long {
        return offset
    }

    override fun getPageSize(): Int {
        return limit
    }

    override fun getPageNumber(): Int {
        return (offset / limit).toInt()
    }

    override fun hasPrevious(): Boolean {
        return offset > limit
    }

    override fun first(): UnorderedPagination {
        return UnorderedPagination(0, limit)
    }

    override fun next(): UnorderedPagination {
        return UnorderedPagination(offset + limit, limit)
    }

    override fun previousOrFirst(): UnorderedPagination {
        return UnorderedPagination(max(0, offset - limit), limit)
    }
}