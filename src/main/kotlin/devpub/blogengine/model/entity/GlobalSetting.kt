package devpub.blogengine.model.entity

import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Table

@Entity
@Table(name = "global_settings")
open class GlobalSetting(id: Int = 0): Persistent(id) {
    enum class Code {
        MULTIUSER_MODE, POST_PREMODERATION, STATISTICS_IS_PUBLIC
    }

    @Enumerated(EnumType.STRING)
    open lateinit var code: Code

    open lateinit var name: String

    open lateinit var value: String
}