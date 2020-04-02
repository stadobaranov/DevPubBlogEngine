package devpub.blogengine.model

import devpub.blogengine.model.entity.ModerationStatus

class ModeratedPostPageRequest: PageRequest() {
    var status = ModerationStatus.NEW
}