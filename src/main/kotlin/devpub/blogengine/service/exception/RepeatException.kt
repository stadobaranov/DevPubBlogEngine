package devpub.blogengine.service.exception

class RepeatException(cause: Exception): RuntimeException(cause) {
    override val cause get() = super.cause as Exception
}