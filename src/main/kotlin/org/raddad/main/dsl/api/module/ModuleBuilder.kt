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

package dsl.api.module

import core.dependency.entity.BuildFactory
import core.dependency.entity.Factory
import core.dependency.entity.Metadata
import core.module.entity.Module
import core.module.entity.MutableRegistry
import storage.StorageDB

class ModuleBuilder {
    @PublishedApi
    internal val factoryRegistry: MutableRegistry = StorageDB()

    infix fun add(factory: Factory) {
        factoryRegistry[Metadata(factory.contract, factory.name)] = factory
    }

    infix fun factory(block: BuildFactory) = this add dsl.api.dependency.factory(block = block)

    operator fun Factory.unaryPlus() {
        factoryRegistry[Metadata(this.contract, this.name)] = this
    }

    fun build(): Module = Module(factoryRegistry)
}
