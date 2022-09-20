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

package org.raddad.main.dsl.api.injector

import org.raddad.main.core.dependency.entity.Factory
import org.raddad.main.core.dependency.entity.Metadata
import org.raddad.main.core.warehouse.entity.Warehouse
import org.raddad.main.dsl.api.dependency.InstanceResolver.resolveInstance
import kotlin.reflect.KClass

class DependencyResolver(val warehouse: Warehouse) {

    /**
     * Resolve a dependency from the warehouse.
     *
     * @param target the class of the dependency.
     * @param <T> the type of the dependency.
     * @return the dependency.
     */
    @PublishedApi
    internal inline fun <reified T> resolveAccessibility(
        entry: Map.Entry<Metadata, Factory>,
        target: KClass<*>
    ): T {
        val factory = entry.value
        val metadata = entry.key
        return if (metadata.dependencyLevel <= 1) {
            if (factory.injectsIn == null || factory.injectsIn.isEmpty())
                resolveInstance(factory)
            else if (factory.injectsIn.contains(target))
                resolveInstance(factory)
            else error(
                "class ${T::class.simpleName} can't be " +
                        "injected in ${target.simpleName} unless you declaratively say so"
            )
        } else {
            error(
                "class ${T::class.simpleName} can't be " +
                        "injected in ${target.simpleName} because Local warehouse factories" +
                        " is not accessible at ${metadata.dependencyLevel} levels down in the dependency tree"
            )
        }
    }

    /*
     * get the dependency factory from the warehouse by its metadata.
     */
    @PublishedApi
    internal fun getDependency(key: Metadata) = warehouse.getFactory(key)
        ?: error("cannot get instance of ${key.classType}")

    /*
     *resolve the dependency from the warehouse by its metadata.
     */
    @PublishedApi
    internal inline fun <reified T> resolve(key: Metadata): T =
        resolveInstance(getDependency(key))

    /*
     * resolve the factory dependency instance
     */
    @PublishedApi
    internal inline fun <reified T> resolveInstance(value: Factory): T = value.resolveInstance(warehouse)

    //resolve resolveTarget
    @PublishedApi
    internal inline operator fun <reified K, reified T> get(key: Metadata): T {
        val factory = warehouse.getEntry(key) ?: error(
            "${key.classType ?: key.className}" +
                    " doesn't exist in the graph"
        )
        return resolveAccessibility(factory, K::class)
    }

    fun containsDependency(metadata: Metadata): Boolean =
        warehouse.containsDependency(metadata)

}
