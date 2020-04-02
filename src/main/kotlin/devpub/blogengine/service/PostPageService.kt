package devpub.blogengine.service

import devpub.blogengine.model.ModeratedPostPageRequest
import devpub.blogengine.model.ModeratedPostPageResponse
import devpub.blogengine.model.PostPageByDateRequest
import devpub.blogengine.model.PostPageBySearchQueryRequest
import devpub.blogengine.model.PostPageByTagRequest
import devpub.blogengine.model.PostPageRequest
import devpub.blogengine.model.PostPageResponse
import devpub.blogengine.model.PostPageForCurrentUserRequest

interface PostPageService {
    fun get(request: PostPageRequest): PostPageResponse

    fun getBySearchQuery(request: PostPageBySearchQueryRequest): PostPageResponse

    fun getByDate(request: PostPageByDateRequest): PostPageResponse

    fun getByTag(request: PostPageByTagRequest): PostPageResponse

    fun getForCurrentUser(request: PostPageForCurrentUserRequest): PostPageResponse

    fun getForModeration(request: ModeratedPostPageRequest): ModeratedPostPageResponse
}