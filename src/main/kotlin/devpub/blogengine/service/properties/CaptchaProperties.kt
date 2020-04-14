package devpub.blogengine.service.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "blog-engine.captcha")
@ConstructorBinding
open class CaptchaProperties(
    val lifetime: Long
)