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

package core.warehouse.entity

import core.dependency.entity.Factory
import core.dependency.entity.Metadata
import dsl.api.injector.DependencyResolver
import dsl.api.injector.DependencyRetriever
import dsl.api.injector.Injector
import dsl.api.injector.InjectorFactory
import storage.StorageDB

data class Warehouse(
    val accessibility: Accessibility? = null,
    val accessibleTo: Any? = null,
    @PublishedApi
    internal var dependencyRegistry: MutableRegistry = StorageDB(),
    private val injectorFactory: InjectorFactory = InjectorFactory()
) : () -> Injector {
    private val dependencyResolver: DependencyResolver = DependencyResolver(this)
    private val dependencyRetriever: DependencyRetriever = DependencyRetriever(this, dependencyResolver)

    private val injector: Injector = injectorFactory(dependencyResolver, dependencyRetriever)

    override fun invoke(): Injector = injector

    fun getFactory(key: Metadata): Factory? = dependencyRegistry[key]
    fun containsDependency(metadata: Metadata) = dependencyRegistry.containsKey(metadata)

    @PublishedApi
    internal inline fun <reified T> contains() = containsDependency(Metadata(T::class))

}
