package devpub.blogengine.model

import com.fasterxml.jackson.annotation.JsonAnySetter
import devpub.blogengine.model.entity.GlobalSetting
import java.util.EnumMap

class UpdateGlobalSettingsRequest {
    @JsonAnySetter val values: Map<GlobalSetting.Code, Any?> = EnumMap(GlobalSetting.Code::class.java)
}