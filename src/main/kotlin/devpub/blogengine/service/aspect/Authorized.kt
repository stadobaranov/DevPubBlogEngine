package devpub.blogengine.service.aspect

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Authorized(
    val dirty: Boolean = true,
    val moderator: Boolean = false
)