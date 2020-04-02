package devpub.blogengine.model.converter

import devpub.blogengine.model.PostOrder
import org.springframework.stereotype.Component

@Component
open class StringToPostOrderConverter: StringToEnumConverter<PostOrder>(PostOrder::class, PostOrder.RECENT)