package devpub.blogengine.model

import com.fasterxml.jackson.annotation.JsonCreator

enum class ModerationDecision {
    ACCEPT, DECLINE;

    companion object {
        @JsonCreator
        @JvmStatic
        fun fromString(name: String): ModerationDecision? {
            values().forEach {
                if(name.equals(it.name, true)) {
                    return it
                }
            }

            return null
        }
    }
}