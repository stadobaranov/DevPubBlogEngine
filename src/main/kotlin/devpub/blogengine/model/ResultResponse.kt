package devpub.blogengine.model

import com.fasterxml.jackson.annotation.JsonProperty

class ResultResponse private constructor(
    @JsonProperty("result") val value: Boolean
) {
    companion object {
        private val FALSE = ResultResponse(false)
        private val TRUE = ResultResponse(true)

        fun of(value: Boolean): ResultResponse {
            return if(value) TRUE else FALSE
        }
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return other is ResultResponse && value == other.value
    }
}