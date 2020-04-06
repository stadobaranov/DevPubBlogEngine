package devpub.blogengine.model

import devpub.blogengine.model.request.NullIfEmptyFileProperty
import devpub.blogengine.model.request.PropertyAlias
import devpub.blogengine.model.validation.Image

class UpdateUserProfileWithAvatarRequest: UpdateUserProfileRequest() {
    override var avatar by NullIfEmptyFileProperty()

    @Deprecated("Параметр для запроса")
    @get:Image
    var photo by PropertyAlias(this::avatar)
}