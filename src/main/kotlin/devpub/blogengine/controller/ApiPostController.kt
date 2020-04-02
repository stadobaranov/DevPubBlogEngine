package devpub.blogengine.controller

import devpub.blogengine.model.PostPageByDateRequest
import devpub.blogengine.model.PostPageBySearchQueryRequest
import devpub.blogengine.model.PostPageByTagRequest
import devpub.blogengine.model.PostPageRequest
import devpub.blogengine.model.PostPageResponse
import devpub.blogengine.service.PostPageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("api/post")
open class ApiPostController @Autowired constructor(
    private val postPageService: PostPageService
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
}