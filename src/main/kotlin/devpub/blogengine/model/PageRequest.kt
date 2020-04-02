package devpub.blogengine.model

open class PageRequest {
    companion object {
        const val DEFAULT_OFFSET = 0
        const val DEFAULT_LIMIT = 10
        const val MAX_LIMIT = 100
    }

    var offset = DEFAULT_OFFSET
        set(value) {
            field = if(value >= 0) value else DEFAULT_OFFSET
        }

    var limit = DEFAULT_LIMIT
        set(value) {
            field = if(value in 1..MAX_LIMIT) value else DEFAULT_LIMIT
        }
}