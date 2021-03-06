package devpub.blogengine

import devpub.blogengine.service.UploadStorage
import devpub.blogengine.service.properties.UploadProperties
import org.springframework.boot.web.server.ErrorPage
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
open class ApplicationConfiguration(
    private val uploadProperties: UploadProperties
): WebMvcConfigurer {
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("${UploadStorage.BASE_URL}**")
                .addResourceLocations("file:${uploadProperties.directory}")
    }

    override fun addViewControllers(registry: ViewControllerRegistry) {
        registry.addViewController("/notFound").setViewName("forward:/")
    }

    @Bean
    open fun servletContainerCustomizer(): WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {
        return WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {
            factory -> factory.addErrorPages(ErrorPage(HttpStatus.NOT_FOUND, "/notFound"))
        }
    }
}