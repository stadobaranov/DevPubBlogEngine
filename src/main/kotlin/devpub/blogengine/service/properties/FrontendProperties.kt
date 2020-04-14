package devpub.blogengine.service.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "blog-engine.frontend")
@ConstructorBinding
open class FrontendProperties(
    val title: String,
    val subTitle: String,
    val contactPhone: String,
    val contactEmail: String,
    val copyright: String,
    val copyrightFrom: String
)