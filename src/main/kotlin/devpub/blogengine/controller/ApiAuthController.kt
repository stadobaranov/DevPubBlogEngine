package devpub.blogengine.controller

import devpub.blogengine.model.AuthorizedUserResponse
import devpub.blogengine.model.GenerateCaptchaResponse
import devpub.blogengine.model.LoginUserRequest
import devpub.blogengine.model.ResultResponse
import devpub.blogengine.service.CaptchaService
import devpub.blogengine.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("api/auth")
open class ApiAuthController @Autowired constructor(
    private val captchaService: CaptchaService,
    private val userService: UserService
) {
    @GetMapping("captcha")
    open fun generateCaptcha(): GenerateCaptchaResponse {
        return captchaService.generate()
    }

    @GetMapping("check")
    open fun checkUserLoggedIn(): AuthorizedUserResponse {
        return userService.checkLoggedIn()
    }

    @PostMapping("login")
    open fun loginUser(@Valid @RequestBody request: LoginUserRequest): AuthorizedUserResponse {
        return userService.login(request)
    }

    @GetMapping("logout")
    open fun logoutUser(): ResultResponse {
        return userService.logout()
    }
}