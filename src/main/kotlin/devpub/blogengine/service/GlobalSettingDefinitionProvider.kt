package devpub.blogengine.service

import devpub.blogengine.model.GlobalSettingDefinition
import devpub.blogengine.model.entity.GlobalSetting

interface GlobalSettingDefinitionProvider {
    fun get(code: GlobalSetting.Code): GlobalSettingDefinition
}