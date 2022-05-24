package dsl.api.injector

import core.dependency.entity.Metadata
import core.dependency.entity.Named
import core.warehouse.entity.Warehouse
import kotlin.reflect.KProperty
import kotlin.reflect.full.findAnnotation


class Injector(
    @PublishedApi
    internal val warehouse: Warehouse
) {
    inline operator fun <reified T, reified K> getValue(target: K, property: KProperty<*>): T {
        val name = property.findAnnotation<Named>()?.name

        return if (name != null)
            resolveTarget<K, T>(Metadata(className = name))
        else
            resolveTarget<K, T>(Metadata(classType = T::class))
    }
}
