package devpub.blogengine.service

import devpub.blogengine.model.GlobalSettingsResponse
import devpub.blogengine.model.UpdateGlobalSettingsRequest
import devpub.blogengine.model.entity.GlobalSetting
import kotlin.reflect.KClass

interface GlobalSettingService {
    fun <T: Any> getValue(code: GlobalSetting.Code, type: KClass<T>): T?

    fun get(): GlobalSettingsResponse

    fun update(request: UpdateGlobalSettingsRequest)
}