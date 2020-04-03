package devpub.blogengine.util

object FileUtils {
    fun getExtension(fileName: String): String? {
        val startPos = fileName.lastIndexOf('.')

        if(startPos == -1 || startPos == fileName.length - 1) {
            return null
        }

        return fileName.substring(startPos + 1)
    }
}