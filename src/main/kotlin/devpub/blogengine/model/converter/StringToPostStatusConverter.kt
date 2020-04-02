package devpub.blogengine.model.converter

import devpub.blogengine.model.PostStatus
import org.springframework.stereotype.Component

@Component
open class StringToPostStatusConverter: StringToEnumConverter<PostStatus>(PostStatus::class, PostStatus.INACTIVE)