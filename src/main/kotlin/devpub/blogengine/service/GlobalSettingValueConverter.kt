package devpub.blogengine.service

import devpub.blogengine.model.entity.GlobalSetting

interface GlobalSettingValueConverter {
    fun isValidStringValue(code: GlobalSetting.Code, stringValue: String): Boolean

    fun convertToString(code: GlobalSetting.Code, value: Any?): String

    fun convertFromString(code: GlobalSetting.Code, stringValue: String): Any?
}