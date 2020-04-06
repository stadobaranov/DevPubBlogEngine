package devpub.blogengine.model.entity.projection

import devpub.blogengine.model.entity.TagName

data class PostCountToTagName(val name: TagName, val count: Int) {
    @Deprecated("Конструктор для JPA")
    constructor(name: TagName, count: Long): this(name, count.toInt())
}