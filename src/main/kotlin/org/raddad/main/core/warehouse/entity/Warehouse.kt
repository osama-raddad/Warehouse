package core.warehouse.entity

import core.dependency.entity.Factory
import core.dependency.entity.Metadata
import dsl.api.injector.Injector
import storage.StorageDB

data class Warehouse(
    val accessibility: Accessibility?=null,
    val accessibleTo: Any?=null,
    @PublishedApi
    internal var dependencyRegistry: MutableRegistry = StorageDB()
){
    private val injector: Injector = Injector(this)

    fun inject() = injector

    fun getFactory(key: Metadata): Factory? = dependencyRegistry[key]
    fun containsDependency(metadata: Metadata) = dependencyRegistry.containsKey(metadata)
}
