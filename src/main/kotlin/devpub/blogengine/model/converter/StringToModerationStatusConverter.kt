package devpub.blogengine.model.converter

import devpub.blogengine.model.entity.ModerationStatus
import org.springframework.stereotype.Component

@Component
open class StringToModerationStatusConverter:
    StringToEnumConverter<ModerationStatus>(ModerationStatus::class, ModerationStatus.NEW)