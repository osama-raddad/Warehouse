package dsl.api.dependency

import core.dependency.entity.Constructor
import core.dependency.entity.CreationPattern
import core.dependency.entity.Factory
import core.warehouse.entity.Warehouse
import kotlin.reflect.KClass

class FactoryBuilder(
    private var contractVal: KClass<*>? = null,
    private var nameVal: String? = null,
    private var creationPatternVal: CreationPattern = CreationPattern.NEW,
    private var injectsInVal: MutableList<KClass<*>>?
) {

    @PublishedApi
    internal lateinit var constructor: Constructor<*>

    @PublishedApi
    internal lateinit var tempType: KClass<*>

    @PublishedApi
    internal var paramsVal: MutableList<KClass<*>> = mutableListOf()


    infix fun creation(creationPattern: CreationPattern) = apply {
        this.creationPatternVal = creationPattern
    }

    infix fun contract(contract: KClass<*>) = apply {
        this.contractVal = contract
    }

    infix fun name(name: String) = apply {
        this.nameVal = name
    }

     fun injectsIn(vararg injectsIn: KClass<*>) = apply {
        this.injectsInVal = injectsIn.toMutableList()
    }

    infix fun injectsIn(injectsIn: KClass<*>) = apply {
        if (injectsInVal == null) {
            injectsInVal = mutableListOf(injectsIn)
        } else {
            injectsInVal?.add(injectsIn)
        }
    }

    inline infix fun <reified T : Any> constructor(noinline constructor: Warehouse.() -> T) = apply {
        tempType = T::class
        this.constructor = constructor
    }

    inline fun <reified V> Warehouse.param(): V {
        paramsVal.add(V::class)
        return this().dependencyRetriever.get(V::class, tempType)
    }

    inline fun <reified V> Warehouse.param(name: String): V {
        paramsVal.add(V::class)
        return this().dependencyRetriever.get(name, tempType)
    }

    fun build(): Factory {
        return Factory(creationPatternVal, contractVal ?: tempType, nameVal, injectsInVal, constructor)
    }
}
