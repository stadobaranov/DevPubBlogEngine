package devpub.blogengine.service

import devpub.blogengine.ApplicationMessages
import devpub.blogengine.model.AuthorizedUserResponse
import devpub.blogengine.model.LoginUserRequest
import devpub.blogengine.model.ResultResponse
import devpub.blogengine.model.UserPasswordReset
import devpub.blogengine.model.entity.User
import devpub.blogengine.repository.UserRepository
import devpub.blogengine.service.aspect.Authorized
import devpub.blogengine.service.exception.DuplicateUserEmailException
import devpub.blogengine.service.exception.DuplicateUserNameException
import devpub.blogengine.service.exception.UserNotFoundException
import devpub.blogengine.service.exception.UserResetCodeExpiredException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

private const val CHANGE_PASSWORD_BASE_URL = "login/change-password/"

@Service
open class UserServiceImpl @Autowired constructor(
    private val authorizationService: AuthorizationService,
    private val passwordHasher: PasswordHasher,
    private val randomStringGenerator: RandomStringGenerator,
    private val urlMaker: UrlMaker,
    private val userRepository: UserRepository
): UserService {
    private fun getCurrentUserId(): Int {
        return authorizationService.getCurrentUserIdOrThrowUnauthorizedException()
    }

    @Transactional(readOnly = true)
    override fun checkLoggedIn(): AuthorizedUserResponse {
        val currentUserId = authorizationService.getCurrentUserId()

        if(currentUserId != null) {
            return AuthorizedUserResponse(userRepository.findAuthorizedSummary(currentUserId))
        }

        return AuthorizedUserResponse(null)
    }

    @Transactional(readOnly = true)
    override fun login(request: LoginUserRequest): AuthorizedUserResponse {
        val summary = userRepository.findAuthorizedSummary(
            request.email, passwordHasher.hash(request.password)
        )

        authorizationService.forgetCurrentSession()

        if(summary != null) {
            authorizationService.authorizeCurrentSessionAs(summary.id)
            return AuthorizedUserResponse(summary)
        }

        return AuthorizedUserResponse(null)
    }

    override fun logout(): ResultResponse {
        authorizationService.forgetCurrentSession()
        return ResultResponse.of(true)
    }

    @Transactional
    override fun create(name: String, email: String, password: String) {
        checkIsUnique(name, email)

        val user = User()
        user.name = name
        user.email = email
        user.passwordHash = passwordHasher.hash(password)
        user.registeredAt = LocalDateTime.now()

        repeatIfThrown(DataIntegrityViolationException::class) {
            userRepository.saveAndFlush(user)
        }
    }

    @Authorized
    @Transactional
    override fun update(name: String?, email: String?, password: String?, removeAvatar: Boolean, avatar: String?) {
        if(name == null && email == null && password == null && !removeAvatar && avatar == null) {
            return
        }

        val currentUserId = getCurrentUserId()
        checkIsUnique(name, email, currentUserId)

        val currentUser = userRepository.findById(currentUserId)
                                        .orElseGet { authorizationService.handleUnAuthorizedAccess() }

        if(name != null) {
            currentUser.name = name
        }

        if(email != null) {
            currentUser.email = email
        }

        if(password != null) {
            currentUser.passwordHash = passwordHasher.hash(password)
        }

        if(removeAvatar) {
            currentUser.avatar = null
        }
        else if(avatar != null) {
            currentUser.avatar = avatar
        }

        repeatIfThrown(DataIntegrityViolationException::class) {
            userRepository.saveAndFlush(currentUser)
        }
    }

    private fun checkIsUnique(name: String?, email: String?, userId: Int? = null) {
        val existingUser = userRepository.findByNameOrEmail(name, email, userId)

        if(existingUser != null) {
            if(existingUser.name == name) {
                throw DuplicateUserNameException(ApplicationMessages.DUPLICATE_USER_NAME)
            }
            else if(existingUser.email == email) {
                throw DuplicateUserEmailException(ApplicationMessages.DUPLICATE_USER_EMAIL)
            }
        }
    }

    @Transactional
    override fun generateResetCode(email: String): UserPasswordReset {
        val user = userRepository.findByEmail(email)

        if(user == null) {
            throw UserNotFoundException(ApplicationMessages.USER_NOT_FOUND)
        }

        user.resetCode = randomStringGenerator.generate(User.RESET_CODE_LENGTH)

        repeatIfThrown(DataIntegrityViolationException::class) {
            userRepository.saveAndFlush(user)
        }

        return UserPasswordReset(user.name, user.email, urlMaker.make("$CHANGE_PASSWORD_BASE_URL${user.resetCode!!}"))
    }

    @Transactional
    override fun changePassword(resetCode: String, newPassword: String) {
        val passwordHash = passwordHasher.hash(newPassword)

        if(userRepository.changePasswordByResetCode(resetCode, passwordHash) == 0) {
            throw UserResetCodeExpiredException(ApplicationMessages.USER_RESET_CODE_EXPIRED)
        }
    }
}