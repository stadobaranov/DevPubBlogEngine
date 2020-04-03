package devpub.blogengine.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
open class UrlMakerImpl(
    @Value("\${blog-engine.host}") private val basePath: String
): UrlMaker {
    override fun make(relativePath: String) = "$basePath$relativePath"
}