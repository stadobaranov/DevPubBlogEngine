package devpub.blogengine.model.pagination

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import java.io.Serializable

abstract class UnorderedPageable: Pageable, Serializable {
    final override fun getSort(): Sort {
        return Sort.unsorted()
    }
}