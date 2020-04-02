package devpub.blogengine.model.request

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty

class PropertyAlias<T>(
    private val backingProperty: KMutableProperty0<T>
): ReadWriteProperty<Any, T> {
    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return backingProperty.get()
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        backingProperty.set(value)
    }
}