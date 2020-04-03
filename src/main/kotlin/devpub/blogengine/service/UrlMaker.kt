package devpub.blogengine.service

interface UrlMaker {
    fun make(relativePath: String): String
}