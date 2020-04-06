package devpub.blogengine.model

import com.fasterxml.jackson.annotation.JsonProperty

data class TagWeightToNamesResponse(
    @JsonProperty("tags") val entries: List<Entry>
) {
    data class Entry(
        val name: String,
        val weight: Float
    )
}