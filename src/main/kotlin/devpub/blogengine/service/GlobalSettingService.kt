package devpub.blogengine.service

import devpub.blogengine.model.GlobalSettingsResponse
import devpub.blogengine.model.UpdateGlobalSettingsRequest

interface GlobalSettingService {
    fun get(): GlobalSettingsResponse

    fun update(request: UpdateGlobalSettingsRequest)
}