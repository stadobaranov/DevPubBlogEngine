package devpub.blogengine.model

class PostPageForCurrentUserRequest: PageRequest() {
    var status = PostStatus.INACTIVE
}