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

/**
 * this enum determines how the library is going to deal with the dependency instance
 */
enum class CreationPattern {
    /**
     * the library will create a new instance every time the object being injected
     */
    NEW,

    /**
     * the library will create a new instance for the first time the object is injected
     * SINGLETON is also thread safe, so it guarantees that there is only one instance
     * even in a threaded environment
     */
    SINGLETON,

    /**
     * the library will create a new instance for the first time the object is injected
     * REUSABLE is not thread safe and in sertan scenarios it can produce multiple instances
     */
    REUSABLE
}
