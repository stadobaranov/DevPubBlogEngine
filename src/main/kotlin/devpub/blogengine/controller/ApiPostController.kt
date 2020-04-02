package devpub.blogengine.controller

import devpub.blogengine.model.PostPageRequest
import devpub.blogengine.model.PostPageResponse
import devpub.blogengine.service.PostPageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/post")
open class ApiPostController @Autowired constructor(
    private val postPageService: PostPageService
) {
    @GetMapping
    open fun getPage(request: PostPageRequest): PostPageResponse {
        return postPageService.get(request)
    }
}