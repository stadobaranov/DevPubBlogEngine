package devpub.blogengine.model.entity

import java.io.Serializable
import javax.persistence.Embeddable

@Embeddable
open class TagName(initValue: String = ""): Serializable {
    var value = initValue.toLowerCase()
        set(string) {
            field = string.toLowerCase()
        }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return other is TagName && value == other.value
    }

    override fun toString(): String {
        return value
    }
}