package devpub.blogengine.service

import devpub.blogengine.model.ResultResponse
import devpub.blogengine.model.UpdateUserProfileRequest

interface UserProfileService {
    fun update(request: UpdateUserProfileRequest): ResultResponse
}