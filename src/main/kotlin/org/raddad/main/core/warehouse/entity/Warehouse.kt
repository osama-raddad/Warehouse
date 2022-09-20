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

/* This class is used to represent a warehouse.
* A warehouse is a collection of factories.
*
* @param accessibility: the accessibility of the warehouse.
* @param accessibleTo: the Type that can access the warehouse.
* @param dependencyRegistry: the list of factories that are in the warehouse.
*/
open class Warehouse(
    val accessibility: Accessibility? = null,
    val accessibleTo: Any? = null,
    @PublishedApi
    internal var dependencyRegistry: MutableRegistry = StorageDB()
) {


    fun getFactory(key: Metadata): Factory? = dependencyRegistry[key]
    fun getEntry(key: Metadata): Map.Entry<Metadata, Factory>? = dependencyRegistry.entries.find { it.key == key }

    /**
     * Register a factory for a given key.
     * @param key the key to register the factory for.
     * @param factory the factory to register.
     */
    fun register(key: Metadata, factory: Factory) = dependencyRegistry.put(key, factory)

    fun containsDependency(metadata: Metadata) = dependencyRegistry.containsKey(metadata)


    @PublishedApi
    internal inline fun <reified T> contains() = containsDependency(Metadata(T::class))

    @PublishedApi
    internal inline fun <reified T> contains(name: String) = containsDependency(Metadata(T::class, className = name))

}
