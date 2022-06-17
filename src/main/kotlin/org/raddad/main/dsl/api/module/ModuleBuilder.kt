package dsl.api.module

import core.dependency.entity.Factory
import core.dependency.entity.Metadata
import core.module.entity.MutableRegistry
import storage.StorageDB
import core.module.entity.Module
import dsl.api.dependency.FactoryBuilder
import kotlin.reflect.KClass

class ModuleBuilder {
    @PublishedApi
    internal val factoryRegistry: MutableRegistry = StorageDB()

    infix fun add(factory: Factory) {
        factoryRegistry[Metadata(factory.contract, factory.name)] = factory
    }

    infix fun factory( block: FactoryBuilder.() -> Unit) = this add dsl.api.dependency.factory(block = block)

    operator fun Factory.unaryPlus() {
        factoryRegistry[Metadata(this.contract, this.name)] = this
    }

    fun build(): Module = Module(factoryRegistry)
}
