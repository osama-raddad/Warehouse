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

package dsl.api.injector

import core.dependency.entity.Factory
import core.dependency.entity.Metadata
import core.warehouse.entity.Warehouse
import dsl.api.dependency.resolveInstance
import kotlin.reflect.KClass

class DependencyResolver(val warehouse: Warehouse) {

    @PublishedApi
    internal inline fun <reified T> resolveAccessibility(
        factory: Factory,
        target: KClass<*>
    ): T =
        if (factory.injectsIn?.contains(target) == true)
            resolveInstance(factory)
        else if (factory.injectsIn == null || factory.injectsIn.isEmpty())
            resolveInstance(factory)
        else error("class ${T::class.simpleName} can't be injected in ${target.simpleName} unless you declaratively say so")

    @PublishedApi
    internal fun getDependency(key: Metadata) = warehouse.getFactory(key)
        ?: error("cannot get instance of ${key.classType}")

    @PublishedApi
    internal inline fun <reified T> resolve(key: Metadata): T =
        resolveInstance(getDependency(key))

    @PublishedApi
    internal inline fun <reified T> resolveInstance(value: Factory): T = value.resolveInstance(warehouse)

    //resolve resolveTarget
    @PublishedApi
    internal inline operator fun <reified K, reified T> get(key: Metadata): T {
        val factory = warehouse.getFactory(key) ?: error("${key.classType ?: key.className} doesn't exist in the graph")
        return resolveAccessibility(factory, K::class)
    }
}
