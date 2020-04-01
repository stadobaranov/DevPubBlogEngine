package devpub.blogengine.controller

import devpub.blogengine.model.InitResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api")
open class ApiGeneralController @Autowired constructor(
    @Value("\${blog-engine.frontend-title}") private val frontendTitle: String,
    @Value("\${blog-engine.frontend-subtitle}") private val frontendSubTitle: String,
    @Value("\${blog-engine.contact-phone}") private val contactPhone: String,
    @Value("\${blog-engine.contact-email}") private val contactEmail: String,
    @Value("\${blog-engine.copyright}") private val copyright: String,
    @Value("\${blog-engine.copyright-from}") private val copyrightFrom: String
) {
    @GetMapping("init")
    open fun init(): InitResponse {
        return InitResponse(frontendTitle, frontendSubTitle, contactPhone, contactEmail, copyright, copyrightFrom)
    }
}