/*
 * Copyright (c) 2022. , Osama Raddad
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

    /**
     * this function allows the user to include other warehouses into the current warehouse
     */
    infix fun add(warehouse: Warehouse) {
        modules.add(warehouse)
    }
    /**
     * this function allows the user to include modules into the current warehouse
     */
    infix fun add(module: Module) = dependencyRegistry.putAll(module.factoryRegistry)

    /**
     * this function allows the user to include modules into the current warehouse
     */
    infix fun module(block: BuildModule) = this add ModuleBuilder().apply(block).build()

    /**
     * this function allows the user to include other warehouses into the current warehouse
     */
    inline infix fun warehouse(block: BuildWarehouse) = modules.add(block())

    /**
     * this function allows the user to include other warehouses into the current warehouse
     */
    operator fun Warehouse.unaryPlus() {
        modules.add(this)
    }

    /**
     * this function allows the user to include modules into the current warehouse
     */
    operator fun Module.unaryPlus() {
        dependencyRegistry.putAll(this.factoryRegistry)
    }

    internal fun build(): Warehouse {
        val warehouse = Warehouse(accessibility, accessibleTo, dependencyRegistry)
        modules.map {
            warehouse.dependencyRegistry.putAll(accessibilityManager.resolveWarehouseAccess(warehouse, it))
        }
        return warehouse
    }
}
