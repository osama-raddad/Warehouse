package core.dependency.entity

import core.warehouse.entity.Warehouse
import dsl.api.dependency.FactoryBuilder
import dsl.api.module.ModuleBuilder
import dsl.api.warehouse.WarehouseBuilder


typealias Constructor<T> = Warehouse.() -> T
typealias BuildWarehouse = WarehouseBuilder.() -> Warehouse
typealias BuildModule = ModuleBuilder.() -> Unit
typealias BuildFactory = FactoryBuilder.() -> Unit
