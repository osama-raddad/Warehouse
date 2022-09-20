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

package org.raddad.main.dsl.api.dependency

import org.raddad.main.core.dependency.entity.CreationPattern
import kotlin.reflect.KClass

/**
 * this function is used to create a factory for a dependency.
 * @param creationPattern is the creation pattern of the dependency.
 * @param contract is the class of the dependency.
 * @param name is the name of the dependency.
 * @param injectsIn is the list of the dependencies that this dependency injects in.
 * @param block is the block of the factory.
 * @return a factory for the dependency.
 */
fun factory(
    contract: KClass<*>? = null,
    name: String? = null,
    creationPattern: CreationPattern = CreationPattern.NEW,
    vararg injectsIn: KClass<*>,
    block: FactoryBuilder.() -> Unit
) = FactoryBuilder(
    contract,
    name,
    creationPattern,
    if (injectsIn.isEmpty()) null else injectsIn.toMutableList()
).apply(block)
    .build()
