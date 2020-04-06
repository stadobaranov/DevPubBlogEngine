package devpub.blogengine.model

import org.springframework.web.multipart.MultipartFile

class UpdateUserProfileWithoutAvatarRequest: UpdateUserProfileRequest() {
    override val avatar: MultipartFile? get() = null
}