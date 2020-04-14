package devpub.blogengine.service

import devpub.blogengine.model.GlobalSettingsResponse
import devpub.blogengine.model.UpdateGlobalSettingsRequest
import devpub.blogengine.model.entity.GlobalSetting
import devpub.blogengine.repository.GlobalSettingRepository
import devpub.blogengine.service.aspect.Authorized
import devpub.blogengine.service.exception.GlobalSettingValueConversionException
import devpub.blogengine.service.properties.GlobalSettingProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.EnumMap
import javax.annotation.PostConstruct
import kotlin.reflect.KClass
import kotlin.reflect.full.cast

@Service
open class GlobalSettingServiceImpl @Autowired constructor(
    private val globalSettingProperties: GlobalSettingProperties,
    private val globalSettingValueConverter: GlobalSettingValueConverter,
    private val globalSettingRepository: GlobalSettingRepository
): GlobalSettingService {
    @PostConstruct
    @Transactional
    open fun prepareEntities() {
        val settings = globalSettingRepository.findAll()

        val settingToCodes = EnumMap<GlobalSetting.Code, GlobalSetting>(GlobalSetting.Code::class.java)
        settings.associateByTo(settingToCodes) { it.code }

        for(code in GlobalSetting.Code.values()) {
            val settingDefinition = globalSettingProperties.getDefinition(code)

            if(settingDefinition == null) {
                error("Определение глобальное настройки для кода \"$code\" не найдено")
            }

            val setting = settingToCodes[code]

            if(setting == null) {
                val defaultValue = settingDefinition.defaultValue

                if(!globalSettingValueConverter.isValidStringValue(code, defaultValue)) {
                    error("Невалидное значение по умолчанию \"$defaultValue\" для кода \"$code\"")
                }

                val newSetting = GlobalSetting()
                newSetting.code = code
                newSetting.value = defaultValue
                newSetting.name = settingDefinition.defaultName
                globalSettingRepository.save(newSetting)
            }
            else {
                val defaultName = settingDefinition.defaultName

                if(defaultName.isNotEmpty() && setting.name != defaultName) {
                    setting.name = defaultName
                    globalSettingRepository.save(setting)
                }

                settingToCodes.remove(code)
            }
        }

        if(settingToCodes.isNotEmpty()) {
            globalSettingRepository.deleteAll(settingToCodes.values.mapTo(hashSetOf<Int>()) { it.id })
        }
    }

    @Transactional(readOnly = true)
    override fun <T: Any> getValue(code: GlobalSetting.Code, type: KClass<T>): T? {
        val setting = globalSettingRepository.findByCode(code)

        if(setting == null) {
            error("Глобальная настройка с кодом \"$code\" не найдена")
        }

        val value = convertFromString(setting)

        if(value == null) {
            return null
        }

        try {
            return type.cast(value)
        }
        catch(exception: TypeCastException) {
            error("Не удалось преобразовать \"$value\" к \"${type.qualifiedName}\"")
        }
    }

    @Authorized(moderator = true)
    @Transactional(readOnly = true)
    override fun get(): GlobalSettingsResponse {
        val settings = globalSettingRepository.findAll()

        val settingValues = EnumMap<GlobalSetting.Code, Any?>(GlobalSetting.Code::class.java)
        settings.associateTo(settingValues) { it.code to convertFromString(it) }

        return GlobalSettingsResponse(settingValues)
    }

    private fun convertFromString(setting: GlobalSetting): Any? {
        try {
            return globalSettingValueConverter.convertFromString(setting.code, setting.value)
        }
        catch(exception: GlobalSettingValueConversionException) {
            error("Не удалось сконвертировать строку \"${setting.value}\" для кода \"${setting.code}\"")
        }
    }

    @Authorized(moderator = true)
    @Transactional
    override fun update(request: UpdateGlobalSettingsRequest) {
        val values = request.values

        if(values.isEmpty()) {
            return
        }

        for((code, value) in values.entries) {
            val stringValue = globalSettingValueConverter.convertToString(code, value)
            globalSettingRepository.update(code, stringValue)
        }
    }
}

