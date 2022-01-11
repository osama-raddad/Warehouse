package dsl.api.dependency

@PublishedApi
internal inline fun <reified T : Any> safeCast(value: Any?): T {
    if (value == null) throw TypeCastException(
        "the DI graph doesn't contain ${T::class.qualifiedName}"
    )
    if (value !is T) throw TypeCastException(
        "${value::class.qualifiedName} cannot be cast to ${T::class.qualifiedName}"
    )
    return value
}
