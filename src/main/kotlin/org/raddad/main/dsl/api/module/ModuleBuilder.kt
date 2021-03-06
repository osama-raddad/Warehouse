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

package org.raddad.main.dsl.api.module

import org.raddad.main.core.dependency.entity.BuildFactory
import org.raddad.main.core.dependency.entity.Factory
import org.raddad.main.core.dependency.entity.Metadata
import org.raddad.main.core.module.entity.Module
import org.raddad.main.core.module.entity.MutableRegistry
import org.raddad.main.storage.StorageDB

class ModuleBuilder {
    @PublishedApi
    internal val factoryRegistry: MutableRegistry = StorageDB()

    /**
     * this function allows the user to include factories into the current module
     */
    infix fun add(factory: Factory) {
        factoryRegistry[Metadata(factory.contract, factory.name, factory.contract.visibility)] = factory
    }

    /**
     * this function allows the user to include factories into the current module
     */
    infix fun factory(block: BuildFactory) = this add org.raddad.main.dsl.api.dependency.factory(block = block)

    /**
     * this function allows the user to include factories into the current module
     */
    operator fun Factory.unaryPlus() {
        factoryRegistry[Metadata(this.contract, this.name, this.contract.visibility)] = this
    }

    internal fun build(): Module = Module(factoryRegistry)
}
