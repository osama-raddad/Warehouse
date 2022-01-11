package dsl.api.warehouse

import core.warehouse.access.AccessibilityManagerContract
import core.warehouse.entity.Accessibility
import core.warehouse.entity.MutableRegistry
import core.warehouse.entity.Warehouse
import storage.StorageDB
import core.module.entity.Module
import dsl.api.injector.get
import kotlin.reflect.KClass

class WarehouseBuilder(
    private val accessibility: Accessibility? = null,
    private val accessibleTo: Any? = null,
    private val accessibilityManager: AccessibilityManagerContract
) {
//Params Should be visible here somehow
    @PublishedApi
    internal var dependencyRegistry: MutableRegistry = StorageDB()

    private val modules: StorageDB<Warehouse, MutableRegistry> = StorageDB()

    infix fun add(warehouse: Warehouse) {
        modules[warehouse] = warehouse.dependencyRegistry
    }

    infix fun add(module: Module) = dependencyRegistry.putAll(module.factoryRegistry)

    fun build(): Warehouse {
        val warehouse = Warehouse(accessibility, accessibleTo, dependencyRegistry)
        warehouse.dependencyRegistry.putAll(accessibilityManager.resolveWarehouseAccess(warehouse, warehouse))
        return warehouse
    }
}
