package devpub.blogengine.service

import devpub.blogengine.service.properties.HostProperties
import org.springframework.stereotype.Service

@Service
open class UrlMakerImpl(
    private val hostProperties: HostProperties
): UrlMaker {
    override fun make(relativePath: String) = "${hostProperties.baseUrl}$relativePath"
}