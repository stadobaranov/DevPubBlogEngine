package devpub.blogengine.controller

import devpub.blogengine.controller.validation.buildMapper
import devpub.blogengine.model.AuthorizedUserResponse
import devpub.blogengine.model.GenerateCaptchaResponse
import devpub.blogengine.model.LoginUserRequest
import devpub.blogengine.model.RegisterUserRequest
import devpub.blogengine.model.ResetUserPasswordRequest
import devpub.blogengine.model.ResultResponse
import devpub.blogengine.model.SendUserResetCodeRequest
import devpub.blogengine.service.CaptchaService
import devpub.blogengine.service.UserPasswordResetService
import devpub.blogengine.service.UserRegistrationService
import devpub.blogengine.service.UserService
import devpub.blogengine.service.ValidationErrorsResponseMaker
import devpub.blogengine.service.exception.DuplicateUserEmailException
import devpub.blogengine.service.exception.DuplicateUserNameException
import devpub.blogengine.service.exception.ProcessingCaptchaException
import devpub.blogengine.service.exception.UserResetCodeExpiredException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
open class ApiAuthController @Autowired constructor(
    private val captchaService: CaptchaService,
    private val userService: UserService,
    private val userPasswordResetService: UserPasswordResetService,
    private val userRegistrationService: UserRegistrationService,
    private val validationErrorsResponseMaker: ValidationErrorsResponseMaker
) {
    @GetMapping("/api/auth/captcha")
    open fun generateCaptcha(): GenerateCaptchaResponse {
        return captchaService.generate()
    }

    @GetMapping("/api/auth/check")
    open fun checkUserLoggedIn(): AuthorizedUserResponse {
        return userService.checkLoggedIn()
    }

    @PostMapping("/api/auth/login")
    open fun loginUser(@Valid @RequestBody request: LoginUserRequest): AuthorizedUserResponse {
        return userService.login(request)
    }

    @GetMapping("/api/auth/logout")
    open fun logoutUser(): ResultResponse {
        return userService.logout()
    }

    @PostMapping("/api/auth/restore")
    open fun restoreUserPassword(@Valid @RequestBody request: SendUserResetCodeRequest): ResultResponse {
        return userPasswordResetService.sendResetCode(request)
    }

    @PostMapping("/api/auth/password")
    open fun resetUserPassword(@Valid @RequestBody request: ResetUserPasswordRequest, bResult: BindingResult): Any {
        if(bResult.hasFieldErrors()) {
            return validationErrorsResponseMaker.makeEntity(request.javaClass.kotlin, bResult.fieldErrors)
        }

        try {
            return userPasswordResetService.reset(request)
        }
        catch(exception: Exception) {
            return validationErrorsResponseMaker.buildMapper(request.javaClass.kotlin)
                .map(ProcessingCaptchaException::class to ResetUserPasswordRequest::captchaCode)
                .map(UserResetCodeExpiredException::class to ResetUserPasswordRequest::resetCode)
                .makeResponseEntity(exception)
        }
    }

    @PostMapping("/api/auth/register")
    open fun registerUser(@Valid @RequestBody request: RegisterUserRequest, bResult: BindingResult): Any {
        if(bResult.hasFieldErrors()) {
            return validationErrorsResponseMaker.makeEntity(request.javaClass.kotlin, bResult.fieldErrors)
        }

        try {
            return userRegistrationService.register(request)
        }
        catch(exception: Exception) {
            return validationErrorsResponseMaker.buildMapper(request.javaClass.kotlin)
                .map(ProcessingCaptchaException::class to RegisterUserRequest::captchaCode)
                .map(DuplicateUserNameException::class to RegisterUserRequest::name)
                .map(DuplicateUserEmailException::class to RegisterUserRequest::email)
                .makeResponseEntity(exception)
        }
    }
}