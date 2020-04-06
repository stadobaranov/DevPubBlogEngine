package devpub.blogengine.service

import devpub.blogengine.ApplicationMessages
import devpub.blogengine.model.entity.GlobalSetting
import devpub.blogengine.service.exception.GlobalSettingValueConversionException
import org.springframework.stereotype.Service

@Service
open class GlobalSettingValueConverterImpl: GlobalSettingValueConverter {
    private val valueConverters = hashMapOf<GlobalSetting.Code, ValueConverter>(
        GlobalSetting.Code.MULTIUSER_MODE to AnswerValueConverter,
        GlobalSetting.Code.POST_PREMODERATION to AnswerValueConverter,
        GlobalSetting.Code.STATISTICS_IS_PUBLIC to AnswerValueConverter
    )

    private fun getConverter(code: GlobalSetting.Code): ValueConverter {
        return valueConverters[code] ?: throwConverterNotFoundException(code)
    }

    override fun isValidStringValue(code: GlobalSetting.Code, stringValue: String): Boolean {
        return getConverter(code).isValidStringValue(stringValue)
    }

    override fun convertToString(code: GlobalSetting.Code, value: Any?): String {
        return getConverter(code).convertToString(value)
    }

    override fun convertFromString(code: GlobalSetting.Code, stringValue: String): Any? {
        return getConverter(code).convertFromString(stringValue)
    }

    private fun throwConverterNotFoundException(code: GlobalSetting.Code): Nothing {
        throw error("Конвертер значений для глобальной настройки с кодом $code не найден")
    }
}

private interface ValueConverter {
    fun isValidStringValue(stringValue: String): Boolean

    fun convertToString(value: Any?): String

    fun convertFromString(stringValue: String): Any?
}

private object AnswerValueConverter: ValueConverter {
    private const val NO = "NO"
    private const val YES = "YES"

    override fun isValidStringValue(stringValue: String): Boolean {
        return stringValue.equals(NO, true) || stringValue.equals(YES, true)
    }

    override fun convertToString(value: Any?): String {
        if(value !is Boolean) {
            throw GlobalSettingValueConversionException(ApplicationMessages.INVALID_GLOBAL_SETTING_VALUE)
        }

        return if(value) YES else NO
    }

    override fun convertFromString(stringValue: String): Any? {
        when {
            stringValue.equals(NO, true) -> return false
            stringValue.equals(YES, true) -> return true
            else -> throw GlobalSettingValueConversionException(ApplicationMessages.INVALID_GLOBAL_SETTING_VALUE)
        }
    }
}