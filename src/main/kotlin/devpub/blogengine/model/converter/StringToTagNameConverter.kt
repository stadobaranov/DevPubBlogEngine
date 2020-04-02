package devpub.blogengine.model.converter

import devpub.blogengine.model.entity.TagName
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class StringToTagNameConverter: Converter<String, TagName> {
    override fun convert(value: String): TagName {
        return TagName(value)
    }
}