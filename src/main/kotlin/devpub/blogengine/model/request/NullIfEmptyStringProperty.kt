package devpub.blogengine.model.request

class NullIfEmptyStringProperty(value: String? = null): NullIfEmptyProperty<String>(value) {
    override fun isEmpty(value: String): Boolean {
        return value.isEmpty()
    }
}