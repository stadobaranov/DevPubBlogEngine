package devpub.blogengine.model

import com.fasterxml.jackson.annotation.JsonFormat
import devpub.blogengine.ApplicationMessages
import devpub.blogengine.model.entity.Post
import devpub.blogengine.model.entity.TagName
import devpub.blogengine.model.validation.HasAnnounce
import devpub.blogengine.model.validation.MaxLength
import devpub.blogengine.model.validation.MinLength
import devpub.blogengine.model.validation.SafeHtml
import devpub.blogengine.model.validation.TagNames
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime
import javax.validation.constraints.NotNull

class SavePostRequest {
    @NotNull(message = ApplicationMessages.FIELD_IS_REQUIRED)
    @MinLength(
        value = Post.MIN_TITLE_LENGTH,
        message = ApplicationMessages.INVALID_MIN_POST_TITLE_LENGTH
    )
    @MaxLength(
        value = Post.MAX_TITLE_LENGTH,
        message = ApplicationMessages.INVALID_MAX_POST_TITLE_LENGTH
    )
    lateinit var title: String

    @NotNull(message = ApplicationMessages.FIELD_IS_REQUIRED)
    @MinLength(
        value = Post.MIN_TEXT_LENGTH,
        message = ApplicationMessages.INVALID_MIN_POST_TEXT_LENGTH
    )
    @MaxLength(
        value = Post.MAX_TEXT_LENGTH,
        message = ApplicationMessages.INVALID_MAX_POST_TEXT_LENGTH
    )
    @SafeHtml
    @HasAnnounce
    lateinit var text: String

    var active = false

    @NotNull(message = ApplicationMessages.FIELD_IS_REQUIRED)
    @TagNames
    lateinit var tags: Set<TagName>

    @NotNull(message = ApplicationMessages.FIELD_IS_REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    lateinit var time: LocalDateTime
}