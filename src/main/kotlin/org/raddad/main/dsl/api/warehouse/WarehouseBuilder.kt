package dsl.api.warehouse

import core.dependency.entity.BuildModule
import core.dependency.entity.BuildWarehouse
import core.module.entity.Module
import core.warehouse.access.AccessibilityManagerContract
import core.warehouse.entity.Accessibility
import core.warehouse.entity.MutableRegistry
import core.warehouse.entity.Warehouse
import dsl.api.module.ModuleBuilder
import storage.StorageDB

class WarehouseBuilder(
    private val accessibility: Accessibility? = null,
    private val accessibleTo: Any? = null,
    private val accessibilityManager: AccessibilityManagerContract
) {

    @PublishedApi
    internal var dependencyRegistry: MutableRegistry = StorageDB()

    @PublishedApi
    internal val modules: MutableList<Warehouse> = mutableListOf()

    infix fun add(warehouse: Warehouse) {
        modules.add(warehouse)
    }

    infix fun add(module: Module) = dependencyRegistry.putAll(module.factoryRegistry)

    infix fun module(block: BuildModule) = this add ModuleBuilder().apply(block).build()

    inline infix fun warehouse(block: BuildWarehouse) = modules.add(block())

    operator fun Warehouse.unaryPlus() {
        modules.add(this)
    }

    operator fun Module.unaryPlus() {
        dependencyRegistry.putAll(this.factoryRegistry)
    }

    fun build(): Warehouse {
        val warehouse = Warehouse(accessibility, accessibleTo, dependencyRegistry)
        modules.map {
            warehouse.dependencyRegistry.putAll(accessibilityManager.resolveWarehouseAccess(warehouse, it))
        }
        return warehouse
    }
}
