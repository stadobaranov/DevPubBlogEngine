package devpub.blogengine.service

interface ModeratorChecker {
    fun check(userId: Int): Boolean?
}