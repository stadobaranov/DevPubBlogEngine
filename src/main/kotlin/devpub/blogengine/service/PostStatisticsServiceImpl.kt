package devpub.blogengine.service

import devpub.blogengine.model.PostStatisticsResponse
import devpub.blogengine.model.entity.GlobalSetting
import devpub.blogengine.service.aspect.Authorized
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
open class PostStatisticsServiceImpl @Autowired constructor(
    private val authorizationService: AuthorizationService,
    private val postService: PostService,
    private val globalSettingService: GlobalSettingService
): PostStatisticsService {
    @Transactional(readOnly = true)
    override fun getForAll(): PostStatisticsResponse {
        if(authorizationService.getCurrentUserId() != null) {
            return postService.getStatistics()
        }
        else if(globalSettingService.getValue(GlobalSetting.Code.STATISTICS_IS_PUBLIC, Boolean::class) == true) {
            return postService.getStatistics()
        }

        authorizationService.handleUnAuthorizedAccess()
    }

    @Authorized
    override fun getForCurrentUser(): PostStatisticsResponse {
        return postService.getStatistics(authorizationService.getCurrentUserIdOrThrowUnauthorizedException())
    }
}