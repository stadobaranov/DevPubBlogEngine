package devpub.blogengine.service

import devpub.blogengine.model.ResetUserPasswordRequest
import devpub.blogengine.model.ResultResponse
import devpub.blogengine.model.SendUserResetCodeRequest

interface UserPasswordResetService {
    fun sendResetCode(request: SendUserResetCodeRequest): ResultResponse

    fun reset(request: ResetUserPasswordRequest): ResultResponse
}