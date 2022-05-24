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
    @PublishedApi
    internal val resolver: DependencyResolver = DependencyResolver(warehouse)

    @PublishedApi
    internal val dependencyRetriever: DependencyRetriever = DependencyRetriever(resolver, warehouse)


    inline operator fun <reified T, reified K> getValue(target: K, property: KProperty<*>): T {
        val name = property.findAnnotation<Named>()?.name
        return resolve<K, T>(name)
    }

    @PublishedApi
    internal inline fun <reified K, reified T> resolve(name: String?): T =
        if (name != null) resolver.get<K, T>(Metadata(className = name))
        else resolver.get<K, T>(Metadata(classType = T::class))


    @PublishedApi
    internal inline fun <reified T> contains() = containsDependency(Metadata(T::class))

    @PublishedApi
    internal fun containsDependency(metadata: Metadata): Boolean =
        warehouse.containsDependency(metadata)
}
