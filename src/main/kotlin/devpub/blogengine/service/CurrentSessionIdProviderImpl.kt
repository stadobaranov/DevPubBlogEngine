package devpub.blogengine.service

import org.springframework.stereotype.Service
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import javax.servlet.http.HttpServletRequest

@Service
open class CurrentSessionIdProviderImpl: CurrentSessionIdProvider {
    override fun get(create: Boolean): String? {
        return getCurrentRequest().getSession(create)?.id
    }

    private fun getCurrentRequest(): HttpServletRequest {
        return (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request
    }
}