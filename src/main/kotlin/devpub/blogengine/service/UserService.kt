package devpub.blogengine.service

import devpub.blogengine.model.AuthorizedUserResponse
import devpub.blogengine.model.LoginUserRequest
import devpub.blogengine.model.ResultResponse
import devpub.blogengine.model.UserPasswordReset

interface UserService {
    fun checkLoggedIn(): AuthorizedUserResponse

    fun login(request: LoginUserRequest): AuthorizedUserResponse

    fun logout(): ResultResponse

    fun create(name: String, email: String, password: String)

    fun update(name: String?, email: String?, password: String?, removeAvatar: Boolean, avatar: String?)

    fun generateResetCode(email: String): UserPasswordReset

    fun changePassword(resetCode: String, newPassword: String)
}