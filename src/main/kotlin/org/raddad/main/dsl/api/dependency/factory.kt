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

package dsl.api.dependency

import core.dependency.entity.CreationPattern
import kotlin.reflect.KClass

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
