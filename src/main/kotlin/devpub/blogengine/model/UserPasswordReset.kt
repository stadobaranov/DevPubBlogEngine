package devpub.blogengine.model

data class UserPasswordReset(
    val userName: String,
    val userEmail: String,
    val link: String
)