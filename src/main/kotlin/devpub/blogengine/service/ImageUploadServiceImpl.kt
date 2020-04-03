package devpub.blogengine.service

import devpub.blogengine.model.UploadImageRequest
import devpub.blogengine.service.aspect.Authorized
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
open class ImageUploadServiceImpl @Autowired constructor(
    private val uploadStorage: UploadStorage
): ImageUploadService {
    @Authorized
    override fun upload(request: UploadImageRequest): String {
        return UploadStorage.getUrl(uploadStorage.store(request.image).replace('\\', '/'))
    }
}