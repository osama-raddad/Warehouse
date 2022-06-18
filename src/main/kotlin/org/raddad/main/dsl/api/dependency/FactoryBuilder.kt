package dsl.api.dependency

import core.dependency.entity.Constructor
import core.dependency.entity.CreationPattern
import core.dependency.entity.Factory
import core.warehouse.entity.Warehouse
import kotlin.reflect.KClass

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

    @PublishedApi
    internal var paramsVal: MutableList<KClass<*>> = mutableListOf()


    infix fun creation(creationPattern: CreationPattern) {
        this.creationPatternVal = creationPattern
    }

    infix fun contract(contract: KClass<*>) {
        this.contractVal = contract
    }

    infix fun name(name: String) {
        this.nameVal = name
    }

    inline infix fun name(block: FactoryBuilder.() -> String) {
        this.nameVal = block()
    }

    fun injectsIn(vararg injectsIn: KClass<*>) {
        this.injectsInVal = injectsIn.toMutableList()
    }

    infix fun injectsIn(injectsIn: KClass<*>) {
        if (injectsInVal == null) {
            injectsInVal = mutableListOf(injectsIn)
        } else {
            injectsInVal?.add(injectsIn)
        }
    }

    inline infix fun creation(block: FactoryBuilder.() -> CreationPattern) {
        this.creationPatternVal = block()
    }

    inline infix fun contract(block: FactoryBuilder.() -> KClass<*>) {
        this.contractVal = block()
    }

    inline fun injectsIn(block: FactoryBuilder.() -> KClass<*>) = injectsIn(block())

    inline infix fun <reified T : Any> constructor(noinline constructor: Warehouse.() -> T) = apply {
        tempType = T::class
        this.constructor = constructor
    }

    inline fun <reified V> Warehouse.param(): V {
        paramsVal.add(V::class)
        return this().dependencyRetriever.get(V::class, tempType)
    }

    inline infix fun <reified V> Warehouse.param(name: String): V {
        paramsVal.add(V::class)
        return this().dependencyRetriever.get(name, tempType)
    }

    operator fun String.unaryPlus() {
        this@FactoryBuilder.nameVal = this
    }

    operator fun KClass<*>.unaryPlus() {
        injectsIn(this)
    }

    fun build(): Factory {
        return Factory(creationPatternVal, contractVal ?: tempType, nameVal, injectsInVal, constructor)
    }
}
