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

package org.raddad.main.core.warehouse.entity

import org.raddad.main.core.dependency.entity.Factory
import org.raddad.main.core.dependency.entity.Metadata
import org.raddad.main.storage.StorageDB

open class  Warehouse(
    val accessibility: Accessibility? = null,
    val accessibleTo: Any? = null,
    @PublishedApi
    internal var dependencyRegistry: MutableRegistry = StorageDB()
)  {


    fun getFactory(key: Metadata): Factory? = dependencyRegistry[key]
    fun containsDependency(metadata: Metadata) = dependencyRegistry.containsKey(metadata)

    @PublishedApi
    internal inline fun <reified T> contains() = containsDependency(Metadata(T::class))

}
