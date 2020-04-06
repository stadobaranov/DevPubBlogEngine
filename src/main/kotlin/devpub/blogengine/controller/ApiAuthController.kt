package devpub.blogengine.controller

import devpub.blogengine.model.AuthorizedUserResponse
import devpub.blogengine.model.GenerateCaptchaResponse
import devpub.blogengine.model.LoginUserRequest
import devpub.blogengine.model.ResetUserPasswordRequest
import devpub.blogengine.model.ResultResponse
import devpub.blogengine.model.SendUserResetCodeRequest
import devpub.blogengine.service.CaptchaService
import devpub.blogengine.service.UserPasswordResetService
import devpub.blogengine.service.UserService
import devpub.blogengine.service.ValidationErrorsResponseMaker
import devpub.blogengine.service.exception.ProcessingCaptchaException
import devpub.blogengine.service.exception.UserResetCodeExpiredException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
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
    private val userService: UserService,
    private val userPasswordResetService: UserPasswordResetService,
    private val validationErrorsResponseMaker: ValidationErrorsResponseMaker
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

    @PostMapping("restore")
    open fun restoreUserPassword(@Valid @RequestBody request: SendUserResetCodeRequest): ResultResponse {
        return userPasswordResetService.sendResetCode(request)
    }

    @PostMapping("password")
    open fun recoverUserPassword(@Valid @RequestBody request: ResetUserPasswordRequest, bResult: BindingResult): Any {
        if(bResult.hasFieldErrors()) {
            return validationErrorsResponseMaker.make(ResetUserPasswordRequest::class, bResult.fieldErrors)
        }

        try {
            return userPasswordResetService.reset(request)
        }
        catch(exception: ProcessingCaptchaException) {
            return validationErrorsResponseMaker.make(
                ResetUserPasswordRequest::class, ResetUserPasswordRequest::captchaCode.name, exception.message!!
            )
        }
        catch(exception: UserResetCodeExpiredException) {
            return validationErrorsResponseMaker.make(
                ResetUserPasswordRequest::class, ResetUserPasswordRequest::resetCode.name, exception.message!!
            )
        }
    }
}