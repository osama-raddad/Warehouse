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

import core.warehouse.access.AccessibilityManager
import core.warehouse.access.AccessibilityManagerContract
import core.warehouse.entity.Accessibility
import kotlin.reflect.KClass

/** warehouse is a factory function for creating a warehouse instance
 * the warehouse instance is the root object in this library it allows you
 * to store your dependencies and also retrieve them.
 *
 * @param accessibleTo receives any as type it is used to group dependencies but more type safe
 * @param accessibilityManager this manager is responsible for handling the access to objects in the graph
 *
 * @see AccessibilityManager
 */
fun warehouse(
    accessibleTo: Any,
    accessibilityManager: AccessibilityManagerContract = AccessibilityManager(),
    block: WarehouseBuilder.() -> Unit,
) = WarehouseBuilder(accessibleTo = accessibleTo, accessibilityManager = accessibilityManager)
    .apply(block)
    .build()

/** warehouse is a factory function for creating a warehouse instance
 * the warehouse instance is the root object in this library it allows you
 * to store your dependencies and also retrieve them.
 *
 * @param accessibility receives Enum of Accessibility as type it is used to group dependencies but more type safe
 * @param accessibilityManager this manager is responsible for handling the access to objects in the graph
 *
 * @see Accessibility
 */
fun warehouse(
    accessibility: Accessibility = Accessibility.LOCAL,
    accessibilityManager: AccessibilityManagerContract = AccessibilityManager(),
    block: WarehouseBuilder.() -> Unit,
) = WarehouseBuilder(accessibility = accessibility, accessibilityManager = accessibilityManager)
    .apply(block)
    .build()

fun setupWarehouse() {
    val kClass: KClass<Any> = Any::class
    kClass.visibility
}
