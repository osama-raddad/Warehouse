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

import org.raddad.main.core.dependency.entity.Constructor
import org.raddad.main.core.dependency.entity.CreationPattern
import org.raddad.main.core.dependency.entity.Factory
import org.raddad.main.core.warehouse.entity.Warehouse
import org.raddad.main.dsl.api.warehouse.InjectableWarehouse
import kotlin.reflect.KClass

/**
 * FactoryBuilder is a DSL for creating factories.
 */
class FactoryBuilder(
    @PublishedApi internal var contractVal: KClass<*>? = null,
    @PublishedApi internal var nameVal: String? = null,
    @PublishedApi internal var creationPatternVal: CreationPattern = CreationPattern.NEW,
    private var injectsInVal: MutableList<KClass<*>>?
) {

    @PublishedApi
    internal lateinit var constructor: Constructor<*>

    @PublishedApi
    internal lateinit var tempType: KClass<*>


    //TODO not used yet can be used for evaluating the warehouse registry
    @PublishedApi
    internal var paramsVal: MutableList<KClass<*>> = mutableListOf()


    /**
     * this function specify how the library is going to deal with the dependency instance
     * @param creationPattern the creation pattern of the dependency
     *
     * @see CreationPattern
     */
    infix fun creation(creationPattern: CreationPattern) {
        this.creationPatternVal = creationPattern
    }

    /**
     * this function specify in which form this object should be injected,
     * and it is mainly used to hide concrete implementations
     * @param contract the contract of the dependency
     */
    infix fun contract(contract: KClass<*>) {
        this.contractVal = contract
    }

    /**
     * this function specify the name of the dependency
     * @param name a string which is used as a key for the given dependency
     * this is useful when you have two or more dependencies of the same type
     */
    infix fun name(name: String) {
        this.nameVal = name
    }

    /**
     * this function takes a lambda with string as a return type
     * this string is used as a key for the given dependency
     * this is useful when you have two or more dependencies of the same type
     * @param block a lambda which returns a string which is used as a name for the given dependency
     */
    inline infix fun name(block: FactoryBuilder.() -> String) {
        this.nameVal = block()
    }

    /**
     * this function specify where the given dependency is going tobe injected
     * @param injectsIn is a list of KClasses of the target receivers
     */
    fun injectsIn(vararg injectsIn: KClass<*>) {
        this.injectsInVal = injectsIn.toMutableList()
    }

    /**
     * this function specify where the given dependency is going tobe injected
     * @param injectsIn is a KClass of the target receiver
     */
    infix fun injectsIn(injectsIn: KClass<*>) {
        if (injectsInVal == null) {
            injectsInVal = mutableListOf(injectsIn)
        } else {
            injectsInVal?.add(injectsIn)
        }
    }

    /**
     * this function specify how the library is going to deal with the dependency instance
     * @param block is a lambda with CreationPattern as a return type
     *
     * @see CreationPattern
     */
    inline infix fun creation(block: FactoryBuilder.() -> CreationPattern) {
        this.creationPatternVal = block()
    }

    /**
     * this function takes a lambda with KClass as a return type
     * this function specify in which form this object should be injected,
     * and it is mainly used to hide concrete implementations
     * @param block a lambda which returns a KClass of the dependency
     */
    inline infix fun contract(block: FactoryBuilder.() -> KClass<*>) {
        this.contractVal = block()
    }

    /**
     * this function takes a lambda with KClass as a return type
     * this function specify where the given dependency is going tobe injected
     * @param block is a lambda with KClass as a return type of the target receiver
     */
    inline fun injectsIn(block: FactoryBuilder.() -> KClass<*>) = injectsIn(block())

    /**
     * this function takes a lambda with T as a return type
     * this function lambda contains the object constructor that allows the library to construct
     * the object when is needed
     * @param constructor is a lambda with KClass as a return type of the target receiver
     */
    inline infix fun <reified T : Any> constructor(noinline constructor: Warehouse.() -> T) = apply {
        tempType = T::class
        this.constructor = constructor
    }

    /**
     * this function notify the library that there is a dependency here that is required for this dependency
     */
    inline fun <reified V> Warehouse.param(): V {
        paramsVal.add(V::class)
        return (this as InjectableWarehouse)().retriever.get(V::class, tempType)
    }

    /**
     * this function notify the library that there is a dependency here that is required for this dependency
     * @param name a String that specify the required dependency name
     * @return the required dependency
     */
    inline infix fun <reified V> Warehouse.param(name: String): V {
        paramsVal.add(V::class)
        return (this as InjectableWarehouse)().retriever.get(name, tempType)
    }

    /**
     * this operator takes string which is used as a key for the given dependency
     * this is useful when you have two or more dependencies of the same type
     */
    operator fun String.unaryPlus() {
        this@FactoryBuilder.nameVal = this
    }

    /**
     * this operator takes KClass which specify where the given dependency is going tobe injected
     */
    operator fun KClass<*>.unaryPlus() {
        injectsIn(this)
    }

    internal fun build(): Factory {
        return Factory(creationPatternVal, contractVal ?: tempType, nameVal, injectsInVal, constructor)
    }
}
