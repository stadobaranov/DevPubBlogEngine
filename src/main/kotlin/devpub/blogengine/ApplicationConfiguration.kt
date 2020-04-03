package devpub.blogengine

import devpub.blogengine.service.UploadStorage
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
open class ApplicationConfiguration(
    @Value("\${blog-engine.upload-dir}") private val uploadDir: String
): WebMvcConfigurer {
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("${UploadStorage.BASE_URL}**")
                .addResourceLocations("file:$uploadDir")
    }
}