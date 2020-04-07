package devpub.blogengine.service

import devpub.blogengine.model.PostStatisticsResponse

interface PostStatisticsService {
    fun getForAll(): PostStatisticsResponse

    fun getForCurrentUser(): PostStatisticsResponse
}