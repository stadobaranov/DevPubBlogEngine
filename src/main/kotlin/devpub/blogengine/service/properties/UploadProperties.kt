package devpub.blogengine.service.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "blog-engine.upload")
@ConstructorBinding
open class UploadProperties(
    val directory: String
)