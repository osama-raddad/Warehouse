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

package org.raddad.main.core.dependency.entity

import org.raddad.main.core.warehouse.entity.Warehouse
import org.raddad.main.dsl.api.dependency.FactoryBuilder
import org.raddad.main.dsl.api.module.ModuleBuilder
import org.raddad.main.dsl.api.warehouse.WarehouseBuilder


typealias Constructor<T> = Warehouse.() -> T
typealias BuildWarehouse = WarehouseBuilder.() -> Warehouse
typealias BuildModule = ModuleBuilder.() -> Unit
typealias BuildFactory = FactoryBuilder.() -> Unit
