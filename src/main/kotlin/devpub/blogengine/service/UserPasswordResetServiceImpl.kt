package devpub.blogengine.service

import devpub.blogengine.model.ResetUserPasswordRequest
import devpub.blogengine.model.ResultResponse
import devpub.blogengine.model.SendUserResetCodeRequest
import devpub.blogengine.service.exception.CaptchaExpiredException
import devpub.blogengine.service.exception.UserNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
open class UserPasswordResetServiceImpl @Autowired constructor(
    private val emailSender: EmailSender,
    private val userService: UserService,
    private val captchaService: CaptchaService
): UserPasswordResetService {
    override fun sendResetCode(request: SendUserResetCodeRequest): ResultResponse {
        try {
            val passwordReset = userService.generateResetCode(request.email)
            emailSender.sendUserPasswordReset(passwordReset)
            return ResultResponse.of(true)
        }
        catch(exception: UserNotFoundException) {
            return ResultResponse.of(false)
        }
    }

    @Transactional(noRollbackFor = [CaptchaExpiredException::class])
    override fun reset(request: ResetUserPasswordRequest): ResultResponse {
        captchaService.process(request.captchaCode, request.captchaSecretCode)
        userService.changePassword(request.resetCode, request.newPassword)
        return ResultResponse.of(true)
    }
}