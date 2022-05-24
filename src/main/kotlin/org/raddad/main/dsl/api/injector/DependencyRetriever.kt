package dsl.api.injector

import core.dependency.entity.Metadata
import core.warehouse.entity.Warehouse
import kotlin.reflect.KClass

class DependencyRetriever(val dependencyResolver: DependencyResolver, val warehouse: Warehouse) {
    @PublishedApi
    internal inline fun <reified T> get(): T {
        val key = Metadata(classType = T::class)
        return dependencyResolver.resolve(key)
    }

    @PublishedApi
    internal inline fun <reified T> get(dependency: KClass<*>, target: KClass<*>): T {
        val key = Metadata(classType = dependency)
        val factory = warehouse.getFactory(key) ?: error("${key.classType ?: key.className} doesn't exist in the graph")
        return dependencyResolver.resolveAccessibility(factory, target = target)
    }

    @PublishedApi
    internal inline fun <reified T> get(name: String, target: KClass<*>): T {
        val key = Metadata(className = name)
        val factory = warehouse.getFactory(key) ?: error("${key.classType ?: key.className} doesn't exist in the graph")
        return dependencyResolver.resolveAccessibility(factory, target = target)
    }

    @PublishedApi
    internal inline fun <reified T> get(name: String): T {
        return dependencyResolver.resolve(Metadata(className = name))
    }
}
