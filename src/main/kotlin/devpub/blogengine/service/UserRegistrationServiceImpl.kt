package devpub.blogengine.service

import devpub.blogengine.model.RegisterUserRequest
import devpub.blogengine.model.ResultResponse
import devpub.blogengine.service.exception.CaptchaExpiredException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
open class UserRegistrationServiceImpl @Autowired constructor(
    private val userService: UserService,
    private val captchaService: CaptchaService
): UserRegistrationService {
    @Transactional(noRollbackFor = [CaptchaExpiredException::class])
    override fun register(request: RegisterUserRequest): ResultResponse {
        captchaService.process(request.captchaCode, request.captchaSecretCode)
        userService.create(request.name, request.email, request.password)
        return ResultResponse.of(true)
    }
}