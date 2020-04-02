package devpub.blogengine.model.entity

import javax.persistence.AttributeOverride
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "tags")
open class Tag(id: Int = 0): Persistent(id) {
    companion object {
        const val MAX_NAME_LENGTH = 255
    }

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "name"))
    open lateinit var name: TagName
}