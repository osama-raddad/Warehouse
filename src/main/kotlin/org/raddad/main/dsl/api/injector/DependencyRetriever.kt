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

import org.raddad.main.core.dependency.entity.Metadata
import org.raddad.main.core.warehouse.entity.Warehouse
import kotlin.reflect.KClass

class DependencyRetriever(val warehouse: Warehouse, val dependencyResolver: DependencyResolver) {
    @PublishedApi
    internal inline fun <reified T> get(): T {
        val key = Metadata(classType = T::class)
        return dependencyResolver.resolve(key)
    }

    @PublishedApi
    internal inline fun <reified T> get(dependency: KClass<*>, target: KClass<*>): T {
        val key = Metadata(classType = dependency)
        val factory = warehouse.getFactory(key) ?: error("${key.classType ?: key.className} doesn't exist in the graph")
        return dependencyResolver.resolveAccessibility(factory, target = target)
    }

    @PublishedApi
    internal inline fun <reified T> get(name: String, target: KClass<*>): T {
        val key = Metadata(className = name)
        val factory = warehouse.getFactory(key) ?: error("${key.classType ?: key.className} doesn't exist in the graph")
        return dependencyResolver.resolveAccessibility(factory, target = target)
    }

    @PublishedApi
    internal inline fun <reified T> get(name: String): T {
        return dependencyResolver.resolve(Metadata(className = name))
    }
}
