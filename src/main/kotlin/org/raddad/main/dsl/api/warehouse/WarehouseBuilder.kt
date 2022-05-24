package dsl.api.warehouse

import core.warehouse.access.AccessibilityManagerContract
import core.warehouse.entity.Accessibility
import core.warehouse.entity.MutableRegistry
import core.warehouse.entity.Warehouse
import storage.StorageDB
import core.module.entity.Module

class WarehouseBuilder(
    private val accessibility: Accessibility? = null,
    private val accessibleTo: Any? = null,
    private val accessibilityManager: AccessibilityManagerContract
) {

    @PublishedApi
    internal var dependencyRegistry: MutableRegistry = StorageDB()

    private val modules: MutableList<Warehouse> = mutableListOf()

    infix fun add(warehouse: Warehouse) {
        modules.add(warehouse)
    }

    infix fun add(module: Module) = dependencyRegistry.putAll(module.factoryRegistry)

    fun build(): Warehouse {
        val warehouse = Warehouse(accessibility, accessibleTo, dependencyRegistry)
        modules.map{
            warehouse.dependencyRegistry.putAll(accessibilityManager.resolveWarehouseAccess(warehouse, it))
        }
        return warehouse
    }
}
