package devpub.blogengine.service

import devpub.blogengine.model.GlobalSettingDefinition
import devpub.blogengine.model.entity.GlobalSetting
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Service

@Service
@ConfigurationProperties(prefix = "blog-engine.global-settings")
open class GlobalSettingDefinitionProviderImpl: GlobalSettingDefinitionProvider {
    var definitions: Map<GlobalSetting.Code, GlobalSettingDefinition>? = null

    override fun get(code: GlobalSetting.Code): GlobalSettingDefinition {
        val thisDefinition = definitions

        if(thisDefinition == null) {
            throwNoSuchDefinitionException(code)
        }

        return thisDefinition[code] ?: throwNoSuchDefinitionException(code)
    }

    private fun throwNoSuchDefinitionException(code: GlobalSetting.Code): Nothing {
        error("Определение глобальное настройки для кода \"$code\" не найдено")
    }
}