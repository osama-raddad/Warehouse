package dsl.api.module


fun module( block: ModuleBuilder.() -> Unit) = ModuleBuilder().apply(block).build()
