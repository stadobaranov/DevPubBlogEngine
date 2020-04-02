package devpub.blogengine.service

import devpub.blogengine.ApplicationMessages
import devpub.blogengine.model.AuthorizedUser
import devpub.blogengine.service.exception.UnauthorizedException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
open class AuthorizationServiceImpl @Autowired constructor(
    private val currentSessionIdProvider: CurrentSessionIdProvider,
    private val moderatorChecker: ModeratorChecker
): AuthorizationService {
    private val sessionToUserIds = ConcurrentHashMap<String, Int>()
    private val executionContextHolder = ThreadLocal<ExecutionContext>()

    override fun getCurrentUserId(dirty: Boolean): Int? {
        return getCurrentUserId(executionContextHolder.get(), dirty)
    }

    private fun getCurrentUserId(executionContext: ExecutionContext?, dirty: Boolean): Int? {
        if(executionContext != null) {
            val user = executionContext.user

            if(user != null) {
                return user.id
            }
            else if(executionContext.userInitialized) {
                return null
            }
        }

        if(dirty) {
            val sessionId = currentSessionIdProvider.get(false)
            return if(sessionId != null) sessionToUserIds[sessionId] else null
        }

        return getCurrentUser(executionContext)?.id
    }

    override fun getCurrentUser(): AuthorizedUser? {
        return getCurrentUser(executionContextHolder.get())
    }

    private fun getCurrentUser(executionContext: ExecutionContext?): AuthorizedUser? {
        if(executionContext == null) {
            return findCurrentUser()
        }

        if(!executionContext.userInitialized) {
            executionContext.user = findCurrentUser()
        }

        return executionContext.user
    }

    private fun findCurrentUser(): AuthorizedUser? {
        val sessionId = currentSessionIdProvider.get(false)

        if(sessionId == null) {
            return null
        }

        val userId = sessionToUserIds[sessionId]

        if(userId == null) {
            return null
        }

        val isModerator = moderatorChecker.check(userId)

        if(isModerator == null) {
            return null
        }

        return AuthorizedUser(userId, isModerator)
    }

    override fun authorizeCurrentSessionAs(userId: Int) {
        if(executionContextHolder.get() != null) {
            throwMethodCalledInsideExecutionContextError()
        }

        sessionToUserIds[currentSessionIdProvider.get()] = userId
    }

    override fun <R> executeIfCurrentSessionAuthorized(dirty: Boolean, moderator: Boolean, execution: () -> R): R {
        var executionContext = executionContextHolder.get()
        val firstCall = executionContext == null

        if(firstCall) {
            executionContext = ExecutionContext()
        }

        if(dirty && !moderator) {
            if(getCurrentUserId(executionContext, true) == null) {
                throwUnauthorizedException()
            }
        }
        else {
            val user = getCurrentUser(executionContext)

            if(user == null || user.isModerator < moderator) {
                throwUnauthorizedException()
            }
        }

        if(firstCall) {
            executionContextHolder.set(executionContext)
        }

        try {
            return execution()
        }
        finally {
            if(firstCall) {
                if(executionContext.forgetCurrentSession) {
                    forgetCurrentSessionIfCreated()
                }

                executionContextHolder.remove()
            }
        }
    }

    override fun handleUnAuthorizedAccess(): Nothing {
        throwUnauthorizedException()
    }

    override fun forgetCurrentSession() {
        val executionContext = executionContextHolder.get()

        if(executionContext != null) {
            executionContext.forgetCurrentSession = true
        }
        else {
            forgetCurrentSessionIfCreated()
        }
    }

    private fun forgetCurrentSessionIfCreated() {
        val sessionId = currentSessionIdProvider.get(false)

        if(sessionId != null) {
            sessionToUserIds.remove(sessionId)
        }
    }

    override fun forgetSession(sessionId: String) {
        if(executionContextHolder.get() != null) {
            throwMethodCalledInsideExecutionContextError()
        }

        sessionToUserIds.remove(sessionId)
    }

    private fun throwUnauthorizedException(): Nothing {
        throw UnauthorizedException(ApplicationMessages.UNAUTHORIZED)
    }

    private fun throwMethodCalledInsideExecutionContextError(): Nothing {
        error("Вызов внутри авторизированного контекста исполнения")
    }
}

private class ExecutionContext {
    var userInitialized = false
        private set

    var user: AuthorizedUser? = null
        set(value) {
            field = value
            userInitialized = true
        }

    var forgetCurrentSession = false
}