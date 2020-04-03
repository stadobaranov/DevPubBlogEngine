package devpub.blogengine.service

import devpub.blogengine.model.UploadImageRequest

interface ImageUploadService {
    fun upload(request: UploadImageRequest): String
}