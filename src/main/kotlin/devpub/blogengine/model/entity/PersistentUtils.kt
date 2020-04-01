package devpub.blogengine.model.entity

object PersistentUtils {
    const val INVALID_ID = -1

    fun isValidId(id: Int): Boolean {
        return id > 0
    }
}