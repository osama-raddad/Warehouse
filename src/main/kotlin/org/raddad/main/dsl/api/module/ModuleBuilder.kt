package dsl.api.module

import core.dependency.entity.Factory
import core.dependency.entity.Metadata
import core.module.entity.MutableRegistry
import storage.StorageDB
import core.module.entity.Module

class ModuleBuilder {
    @PublishedApi
    internal val factoryRegistry: MutableRegistry = StorageDB()

    infix fun add(factory: Factory) {
        factoryRegistry[Metadata(factory.contract, factory.name)] = factory
    }

    fun build(): Module = Module(factoryRegistry)
}
