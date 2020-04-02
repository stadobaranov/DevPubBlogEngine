package devpub.blogengine.controller

import devpub.blogengine.model.DetailedPostResponse
import devpub.blogengine.model.ModeratedPostPageRequest
import devpub.blogengine.model.ModeratedPostPageResponse
import devpub.blogengine.model.PostPageByDateRequest
import devpub.blogengine.model.PostPageBySearchQueryRequest
import devpub.blogengine.model.PostPageByTagRequest
import devpub.blogengine.model.PostPageForCurrentUserRequest
import devpub.blogengine.model.PostPageRequest
import devpub.blogengine.model.PostPageResponse
import devpub.blogengine.model.SavePostRequest
import devpub.blogengine.service.PostPageService
import devpub.blogengine.service.PostService
import devpub.blogengine.service.ValidationErrorsResponseMaker
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("api/post")
open class ApiPostController @Autowired constructor(
    private val postPageService: PostPageService,
    private val postService: PostService,
    private val validationErrorsResponseMaker: ValidationErrorsResponseMaker
) {
    @GetMapping
    open fun getPage(request: PostPageRequest): PostPageResponse {
        return postPageService.get(request)
    }

    @GetMapping("search")
    open fun getPageBySearchQuery(request: PostPageBySearchQueryRequest): PostPageResponse {
        return postPageService.getBySearchQuery(request)
    }

    @GetMapping("byDate")
    open fun getPageByDate(@Valid request: PostPageByDateRequest): PostPageResponse {
        return postPageService.getByDate(request)
    }

    @GetMapping("byTag")
    open fun getPageByTag(@Valid request: PostPageByTagRequest): PostPageResponse {
        return postPageService.getByTag(request)
    }

    @GetMapping("my")
    open fun getPageForCurrentUser(request: PostPageForCurrentUserRequest): PostPageResponse {
        return postPageService.getForCurrentUser(request)
    }

    @GetMapping("moderation")
    open fun getPageForModeration(request: ModeratedPostPageRequest): ModeratedPostPageResponse {
        return postPageService.getForModeration(request)
    }

    @GetMapping("{id}")
    open fun getDetails(@PathVariable("id") id: Int): DetailedPostResponse {
        return postService.getDetails(id)
    }

    @PostMapping
    open fun create(@Valid @RequestBody request: SavePostRequest, bResult: BindingResult): Any {
        if(bResult.hasFieldErrors()) {
            return validationErrorsResponseMaker.make(SavePostRequest::class, bResult.fieldErrors)
        }

        return postService.create(request)
    }

    @PutMapping("{id}")
    open fun update(
        @PathVariable("id") id: Int,
        @Valid @RequestBody request: SavePostRequest,
        bResult: BindingResult
    ): Any {
        if(bResult.hasFieldErrors()) {
            return validationErrorsResponseMaker.make(SavePostRequest::class, bResult.fieldErrors)
        }

        return postService.update(id, request)
    }
}