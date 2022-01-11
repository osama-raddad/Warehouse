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
