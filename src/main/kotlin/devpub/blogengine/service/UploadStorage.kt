package devpub.blogengine.service

import org.springframework.web.multipart.MultipartFile

interface UploadStorage {
    companion object {
        const val BASE_URL = "/upload/"

        fun getUrl(relativeUrl: String): String {
            return "$BASE_URL$relativeUrl"
        }
    }

    fun store(file: MultipartFile): String

    fun remove(filePath: String)
}