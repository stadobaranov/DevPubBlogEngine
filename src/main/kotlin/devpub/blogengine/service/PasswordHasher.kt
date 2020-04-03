package devpub.blogengine.service

interface PasswordHasher {
    fun hash(password: String): String
}