package devpub.blogengine.service

import devpub.blogengine.model.AuthorizedUser

interface AuthorizationService {
    fun getCurrentUserIdOrThrowUnauthorizedException(dirty: Boolean = true): Int {
        return getCurrentUserId(dirty) ?: handleUnAuthorizedAccess()
    }

    fun getCurrentUserId(dirty: Boolean = true): Int?

    fun getCurrentUserOrThrowUnauthorizedException(): AuthorizedUser {
        return getCurrentUser() ?: handleUnAuthorizedAccess()
    }

    fun getCurrentUser(): AuthorizedUser?

    fun authorizeCurrentSessionAs(userId: Int)

    fun <R> executeIfCurrentSessionAuthorized(dirty: Boolean, moderator: Boolean, execution: () -> R): R

    fun handleUnAuthorizedAccess(): Nothing

    fun forgetCurrentSession()

    fun forgetSession(sessionId: String)
}