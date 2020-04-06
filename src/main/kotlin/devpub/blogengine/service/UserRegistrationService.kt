package devpub.blogengine.service

import devpub.blogengine.model.RegisterUserRequest
import devpub.blogengine.model.ResultResponse

interface UserRegistrationService {
    fun register(request: RegisterUserRequest): ResultResponse
}