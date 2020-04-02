package devpub.blogengine.model

import devpub.blogengine.ApplicationMessages
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import javax.validation.constraints.NotNull

class PostPageByDateRequest: PageRequest() {
    @NotNull(message = ApplicationMessages.FIELD_IS_REQUIRED)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    lateinit var date: LocalDate
}