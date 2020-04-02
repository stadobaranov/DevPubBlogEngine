package devpub.blogengine.service.aspect

import devpub.blogengine.service.AuthorizationService
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.core.Ordered
import org.springframework.stereotype.Component

@Aspect
@Component
open class AuthorizationAspect(
    private val authorizationService: AuthorizationService
): Ordered {
    override fun getOrder(): Int {
        return AspectOrders.AUTHORIZATION_ORDER
    }

    @Around("@annotation(authorized)")
    open fun executeIfAuthorized(joinPoint: ProceedingJoinPoint, authorized: Authorized): Any? {
        return authorizationService.executeIfCurrentSessionAuthorized(authorized.dirty, authorized.moderator) {
            return@executeIfCurrentSessionAuthorized joinPoint.proceed()
        }
    }
}