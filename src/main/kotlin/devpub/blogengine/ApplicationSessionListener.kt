package devpub.blogengine

import devpub.blogengine.service.AuthorizationService
import org.springframework.beans.factory.annotation.Autowired
import javax.servlet.annotation.WebListener
import javax.servlet.http.HttpSessionEvent
import javax.servlet.http.HttpSessionListener

private const val MAX_INACTIVE_INTERVAL = 60 * 60 * 24 * 7

@WebListener
open class ApplicationSessionListener @Autowired constructor(
    private val authorizationService: AuthorizationService
): HttpSessionListener {
    override fun sessionCreated(event: HttpSessionEvent) {
        event.session.maxInactiveInterval = MAX_INACTIVE_INTERVAL
    }

    override fun sessionDestroyed(event: HttpSessionEvent) {
        authorizationService.forgetSession(event.session.id)
    }
}