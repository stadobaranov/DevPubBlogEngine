package devpub.blogengine.model

import com.fasterxml.jackson.annotation.JsonAnyGetter
import devpub.blogengine.model.entity.GlobalSetting

data class GlobalSettingsResponse(
    @get:JsonAnyGetter val values: Map<GlobalSetting.Code, Any?>
)