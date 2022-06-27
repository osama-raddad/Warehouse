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

import core.dependency.entity.Metadata
import core.dependency.entity.Named
import kotlin.reflect.KProperty
import kotlin.reflect.full.findAnnotation


class Injector(
    @PublishedApi
    internal val resolver: DependencyResolver,
    @PublishedApi
    internal val retriever: DependencyRetriever,
) {


    inline operator fun <reified T, reified K> getValue(target: K, property: KProperty<*>): T {
        val name = property.findAnnotation<Named>()?.name
        return resolve<K, T>(name)
    }

    @PublishedApi
    internal inline fun <reified K, reified T> resolve(name: String?): T =
        if (name != null) resolver.get<K, T>(Metadata(className = name))
        else resolver.get<K, T>(Metadata(classType = T::class))


    @PublishedApi
    internal inline fun <reified T> contains() =
        resolver.containsDependency(Metadata(T::class))
}
