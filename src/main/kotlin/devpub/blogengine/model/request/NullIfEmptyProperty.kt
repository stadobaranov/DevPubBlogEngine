package devpub.blogengine.model.request

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class NullIfEmptyProperty<T>(
    private var value: T? = null
): ReadWriteProperty<Any, T?> {
    protected abstract fun isEmpty(value: T): Boolean

    override fun getValue(thisRef: Any, property: KProperty<*>): T? {
        return value
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) {
        this.value = if(value == null || isEmpty(value)) null else value
    }
}