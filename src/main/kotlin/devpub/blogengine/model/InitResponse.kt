package devpub.blogengine.model

data class InitResponse(
    val title: String,
    val subtitle: String,
    val phone: String,
    val email: String,
    val copyright: String,
    val copyrightFrom: String
)