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

package org.raddad.main.core.dependency.entity

import kotlin.reflect.KClass

data class Factory(
    @PublishedApi
    internal val creationPattern: CreationPattern,
    @PublishedApi
    internal val contract: KClass<*>,
    @PublishedApi
    internal val name: String? = null,
    @PublishedApi
    internal val injectsIn: List<KClass<*>>? = null,
    @PublishedApi
    internal val constructor: Constructor<*>,
    @PublishedApi
    internal var instance: Any? = null
)
