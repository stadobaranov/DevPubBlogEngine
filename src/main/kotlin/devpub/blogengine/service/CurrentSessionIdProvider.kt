package devpub.blogengine.service

interface CurrentSessionIdProvider {
    fun get(): String {
        return get(true)!!
    }

    fun get(create: Boolean): String?
}