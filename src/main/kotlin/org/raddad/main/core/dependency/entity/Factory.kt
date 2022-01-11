package core.dependency.entity

import kotlin.reflect.KClass

data class Factory(@PublishedApi
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
                       internal var instance: Any? = null)
