package devpub.blogengine.service.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "blog-engine.host")
@ConstructorBinding
open class HostProperties(
    val baseUrl: String
)