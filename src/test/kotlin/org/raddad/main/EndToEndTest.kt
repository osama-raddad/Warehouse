package org.raddad.main

import org.raddad.main.core.dependency.entity.CreationPattern
import org.raddad.main.core.dependency.entity.Factory
import org.raddad.main.dsl.api.dependency.factory
import org.raddad.main.dsl.api.module.ModuleBuilder
import org.raddad.main.dsl.api.warehouse.InjectableWarehouse
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail

class EndToEndTest {
    private val warehouse = InjectableWarehouse()

    private fun addFactory(factory: Factory) {
        warehouse.dependencyRegistry.putAll(
            ModuleBuilder().apply { this add factory }.build()
                .factoryRegistry
        )
    }


    @Test
    fun `Test add dependency`() {
        val fakeDependency = "fakeDependency1"
        addFactory(factory {
            this constructor { fakeDependency }
        })

        Assertions.assertTrue(warehouse.contains<String>(), "fail to add dependency")
    }

    @Test
    fun `should create a factory`() {
        val factory = factory {
            constructor { String::class }
            creation { CreationPattern.SINGLETON }
            name {"test"}
        }
        addFactory(factory)

        Assertions.assertTrue { warehouse.contains<String>("test") }
    }

    @Test
    fun `should create a factory with multiple constructors`() {
        val factory = factory {
            constructor { String::class }
            constructor { Int::class }
            creation { CreationPattern.SINGLETON }
            name {"test"}
        }
        addFactory(factory)

        Assertions.assertTrue { warehouse.contains<String>("test") }
        Assertions.assertTrue { warehouse.contains<Int>("test") }
    }

    @Test
    fun `Test retrieve dependency`() {
        val fakeDependency = "fakeDependency"
        addFactory(factory {
            this constructor { fakeDependency }
        })

        Assertions.assertEquals(
            fakeDependency,
            warehouse().retriever.get<String>(),
            "fail to retrieve dependency"
        )
    }

    @Test
    fun `Test retrieve newInstance dependency`() {
        val fakeDependency = "fakeDependency"

        data class Test(var a: String)
        addFactory(factory {
            constructor { fakeDependency }
        })
        addFactory(factory {
            constructor { Test(warehouse().retriever.get()) }
        })

        val testA: Test = warehouse().retriever.get()
        val testB: Test = warehouse().retriever.get()

        Assertions.assertFalse(testA === testB, "fail to retrieve newInstance dependency")
    }

    @Test
    fun `Test retrieve SINGLETON (declared in builder constructor) dependency`() {
        val fakeDependency = "fakeDependency"

        data class Test(var a: String)

        addFactory(factory {
            constructor { fakeDependency }
        })
        addFactory(factory(creationPattern = CreationPattern.SINGLETON) {
            constructor { Test(warehouse.param()) }
        })
        val testA: Test = warehouse().retriever.get()
        val testB: Test = warehouse().retriever.get()

        Assertions.assertTrue(testA === testB, "fail to retrieve SINGLETON dependency")
    }

    @Test
    fun `Test retrieve SINGLETON (declared after builder constructor) dependency`() {
        val fakeDependency = "fakeDependency"

        data class Test(var a: String)

        addFactory(factory {
            constructor { fakeDependency }
        })
        addFactory(factory {
            constructor { Test(warehouse.param()) }
            this creation CreationPattern.SINGLETON
        })

        val testA: Test = warehouse().retriever.get()
        val testB: Test = warehouse().retriever.get()

        Assertions.assertTrue(testA === testB, "fail to retrieve SINGLETON dependency")
    }

    @Test
    fun `Test retrieve contract Instance (declared in builder constructor) dependency`() {
        val fakeDependency = "fakeDependency"


        data class Test(var a: String) : TestContract

        addFactory(factory {
            constructor { fakeDependency }
        })
        addFactory(factory(contract = TestContract::class) {
            constructor { Test(warehouse().retriever.get()) }
        })

        try {
            warehouse().retriever.get<TestContract>()
        } catch (e: TypeCastException) {
            fail("fail to retrieve contract Instance dependency", e)
        }
    }

    @Test
    fun `Test retrieve contract Instance (declared after builder constructor) dependency`() {
        val fakeDependency = "fakeDependency"

        data class Test(var a: String) : TestContract

        addFactory(factory {
            constructor { fakeDependency }
        })
        addFactory(factory {
            constructor { Test(warehouse.param()) }
            this contract TestContract::class
        })

        try {
            warehouse().retriever.get<TestContract>()
        } catch (e: TypeCastException) {
            fail("fail to retrieve contract Instance dependency", e)
        }
    }

    @Test
    fun `Test retrieve named dependency (declared in builder constructor)`() {
        val name1 = "1"
        val name2 = "2"
        val fakeDependency1 = "fakeDependency1"
        val fakeDependency2 = "fakeDependency2"

        addFactory(factory(name = name1) {
            constructor { fakeDependency1 }
        })
        addFactory(factory(name = name2) {
            constructor { fakeDependency2 }
        })

        val test1: String = warehouse().retriever.get(name1)
        val test2: String = warehouse().retriever.get(name2)

        Assertions.assertEquals(fakeDependency1, test1, "fail to retrieve named dependency")
        Assertions.assertEquals(fakeDependency2, test2, "fail to retrieve named dependency")
        Assertions.assertNotEquals(test1, test2, "fail to retrieve named dependency")
    }

    @Test
    fun `Test retrieve named dependency (declared after builder constructor) dependency`() {
        val name1 = "1"
        val name2 = "2"
        val fakeDependency1 = "fakeDependency1"
        val fakeDependency2 = "fakeDependency2"

        addFactory(factory {
            constructor { fakeDependency1 }
            this name name1
        })
        addFactory(factory {
            constructor { fakeDependency2 }
            this name name2
        })

        val test1: String = warehouse().retriever.get(name1)
        val test2: String = warehouse().retriever.get(name2)

        Assertions.assertEquals(fakeDependency1, test1, "fail to retrieve named dependency")
        Assertions.assertEquals(fakeDependency2, test2, "fail to retrieve named dependency")
        Assertions.assertNotEquals(test1, test2, "fail to retrieve named dependency")
    }
}

interface TestContract
