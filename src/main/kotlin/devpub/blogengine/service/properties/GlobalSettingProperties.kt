package devpub.blogengine.service.properties

import devpub.blogengine.model.entity.GlobalSetting
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "blog-engine.global-settings")
@ConstructorBinding
open class GlobalSettingProperties(
   private val definitions: Map<GlobalSetting.Code, Definition>
) {
    fun getDefinition(code: GlobalSetting.Code): Definition? {
        return definitions[code]
    }

    class Definition(
        val defaultName: String,
        val defaultValue: String
    )
}