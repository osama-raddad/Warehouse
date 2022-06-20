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

package core.warehouse.access

import core.dependency.entity.Factory
import core.dependency.entity.Metadata
import core.module.entity.Registry
import core.warehouse.entity.Accessibility.*
import core.warehouse.entity.Warehouse
import kotlin.reflect.KVisibility

open class AccessibilityManager : AccessibilityManagerContract {


    override fun resolveWarehouseAccess(
        myWarehouse: Warehouse,
        hisWarehouse: Warehouse
    ): Registry {
        return when {
            hasDefaultAccessibility(hisWarehouse) -> resolveAccessibility(myWarehouse, hisWarehouse)
            hasSameAccessibility(myWarehouse, hisWarehouse) -> getPublicDeclarations(hisWarehouse)

            else -> error(
                "failed to add ${hisWarehouse::class.java.name}:(${hisWarehouse.accessibleTo}) " +
                        "to ${myWarehouse::class.java.name}:(${myWarehouse.accessibleTo}) miss matched accessibility"
            )
        }
    }

    private fun resolveAccessibility(
        myWarehouse: Warehouse,
        hisWarehouse: Warehouse
    ): Registry =
        when (hisWarehouse.accessibility) {
            ISOLATED -> error(
                "failed to add ${hisWarehouse::class.java.name}" +
                        "#(${hisWarehouse.accessibility}) " +
                        "to ${myWarehouse::class.java.name}, " +
                        "${hisWarehouse::class.java.name} is ISOLATED there for it cant be added to another warehouses"
            )
            OPEN -> getPublicDeclarations(hisWarehouse)
            LOCAL -> getPublicDeclarations(hisWarehouse).mapKeys {
                it.key.copy(isClosed = true)
            }
            null -> TODO()
        }

    private fun hasDefaultAccessibility(hisWarehouse: Warehouse) = hisWarehouse.accessibleTo == null


    private fun hasSameAccessibility(myWarehouse: Warehouse, hisWarehouse: Warehouse) =
        hisWarehouse.accessibleTo == myWarehouse.accessibleTo

    private fun getPublicDeclarations(warehouse: Warehouse) =
        warehouse.dependencyRegistry
            .filter(::isVisibleDeclaration)

    private fun isVisibleDeclaration(declaration: Map.Entry<Metadata, Factory>): Boolean {
        return declaration.key.classType?.visibility == KVisibility.PUBLIC && !declaration.key.isClosed
    }


}
