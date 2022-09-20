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
import kotlin.reflect.KVisibility

/**
 * Created by Osama Raddad on 3/1/20.
 *
 * This class is used to represent a dependency in the application.
 *
 * @param classType The class of the dependency.
 * @param className The name of the dependency.
 * @param classVisibility The visibility of the dependency.
 */
data class Metadata(
    val classType: KClass<*>? = null,
    val className: String? = null,
    val classVisibility: KVisibility? = classType?.visibility,
    var dependencyLevel: Int = 0,
) {
    override fun toString(): String {
        return "Metadata(classType=$classType, className=$className, classVisibility=$classVisibility, dependencyLevel=$dependencyLevel)"
    }
    
    override fun hashCode() = getHashCode()

    override fun equals(other: Any?): Boolean = getHashCode() == other.hashCode()

    private fun getHashCode() = className?.hashCode() ?: classType.hashCode()
}
