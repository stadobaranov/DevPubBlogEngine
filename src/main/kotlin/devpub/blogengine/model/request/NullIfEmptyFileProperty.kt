package devpub.blogengine.model.request

import org.springframework.web.multipart.MultipartFile

class NullIfEmptyFileProperty: NullIfEmptyProperty<MultipartFile>() {
    override fun isEmpty(value: MultipartFile): Boolean {
        return value.isEmpty
    }
}