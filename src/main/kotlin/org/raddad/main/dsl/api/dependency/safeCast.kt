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

/*
   * this function is used cast the dependency to the type of the dependency or throw an exception if the dependency is not of the type.
 */
@PublishedApi
internal inline fun <reified T : Any> safeCast(value: Any?): T {
    if (value == null) throw TypeCastException(
        "the org.raddad.main.DI graph doesn't contain ${T::class.qualifiedName}"
    )
    if (value !is T) throw TypeCastException(
        "${value::class.qualifiedName} cannot be cast to ${T::class.qualifiedName}"
    )
    return value
}
