package devpub.blogengine.model

import devpub.blogengine.ApplicationMessages
import devpub.blogengine.model.entity.TagName
import javax.validation.constraints.NotNull

class PostPageByTagRequest: PageRequest() {
    @NotNull(message = ApplicationMessages.FIELD_IS_REQUIRED)
    lateinit var tag: TagName
}