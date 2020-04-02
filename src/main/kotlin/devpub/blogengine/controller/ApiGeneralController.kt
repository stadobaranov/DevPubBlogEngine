package devpub.blogengine.controller

import devpub.blogengine.model.CommentPostRequest
import devpub.blogengine.model.InitResponse
import devpub.blogengine.model.ModeratePostRequest
import devpub.blogengine.model.PostCountToDatesRequest
import devpub.blogengine.model.PostCountToDatesResponse
import devpub.blogengine.service.PostService
import devpub.blogengine.service.ValidationErrorsResponseMaker
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("api")
open class ApiGeneralController @Autowired constructor(
    @Value("\${blog-engine.frontend-title}") private val frontendTitle: String,
    @Value("\${blog-engine.frontend-subtitle}") private val frontendSubTitle: String,
    @Value("\${blog-engine.contact-phone}") private val contactPhone: String,
    @Value("\${blog-engine.contact-email}") private val contactEmail: String,
    @Value("\${blog-engine.copyright}") private val copyright: String,
    @Value("\${blog-engine.copyright-from}") private val copyrightFrom: String,
    private val postService: PostService,
    private val validationErrorsResponseMaker: ValidationErrorsResponseMaker
) {
    @GetMapping("init")
    open fun init(): InitResponse {
        return InitResponse(frontendTitle, frontendSubTitle, contactPhone, contactEmail, copyright, copyrightFrom)
    }

    @GetMapping("calendar")
    open fun getPostCountByDate(request: PostCountToDatesRequest): PostCountToDatesResponse {
        return postService.getCountToDates(request)
    }

    @PostMapping("comment")
    open fun comment(@Valid @RequestBody request: CommentPostRequest, bResult: BindingResult): Any {
        if(bResult.hasFieldErrors()) {
            return validationErrorsResponseMaker.make(CommentPostRequest::class, bResult.fieldErrors)
        }

        return postService.comment(request)
    }

    @PostMapping("moderation")
    open fun moderate(@Valid @RequestBody request: ModeratePostRequest) {
        postService.moderate(request)
    }
}