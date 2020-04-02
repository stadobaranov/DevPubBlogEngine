package devpub.blogengine.service

interface AnnounceExtractor {
    fun extract(text: String): String
}