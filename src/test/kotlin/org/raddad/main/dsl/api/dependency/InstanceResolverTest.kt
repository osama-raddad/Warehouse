package org.raddad.main.dsl.api.dependency

import org.raddad.main.core.dependency.entity.Constructor
import org.raddad.main.core.dependency.entity.CreationPattern
import org.raddad.main.core.dependency.entity.Factory
import org.raddad.main.core.warehouse.entity.Warehouse
import io.mockk.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.raddad.main.dsl.api.dependency.InstanceResolver.resolveInstance

internal class InstanceResolverTest {

    @Test
    fun `Test retrieving a new instance`() {
        val factory: Factory = mockk("factory")
        val warehouse: Warehouse = mockk("warehouse")
        val constructor: Constructor<String> = mockk("constructor")
        val string = "test"

        every { factory.creationPattern } returns CreationPattern.NEW
        every { factory.constructor } returns constructor
        every { constructor.invoke(warehouse) } returns string

        Assertions.assertEquals(string, factory.resolveInstance<String>(warehouse))

        verifySequence {
            factory.creationPattern
            factory.constructor
        }
    }

    @Test
    fun `Test retrieving a null reusable instance`() {
        val factory: Factory = mockk("factory")
        val warehouse: Warehouse = mockk("warehouse")
        val constructor: Constructor<String> = mockk("constructor")
        val string = "test"

        every { factory.creationPattern } returns CreationPattern.REUSABLE
        every { factory.constructor } returns constructor
        every { factory.instance } returns null
        every { constructor.invoke(warehouse) } returns string
        every { factory.instance = string } just runs


        assertThrows(TypeCastException::class.java) { factory.resolveInstance<String>(warehouse) }

        verifySequence {
            factory.creationPattern
            factory.instance
            factory.constructor
            factory.instance = string
            factory.instance
        }
    }


    @Test
    fun `Test retrieving a reusable instance`() {
        val factory: Factory = mockk("factory")
        val warehouse: Warehouse = mockk("warehouse")
        val constructor: Constructor<String> = mockk("constructor")
        val string = "test"

        every { factory.creationPattern } returns CreationPattern.REUSABLE
        every { factory.constructor } returns constructor
        every { factory.instance } returns string
        every { constructor.invoke(warehouse) } returns string
        every { factory.instance = string } just runs


        Assertions.assertEquals(string, factory.resolveInstance<String>(warehouse))

        verifySequence {
            factory.creationPattern
            factory.instance
            factory.instance
        }
    }

    @Test
    fun `Test retrieving a null singleton instance`() {
        val factory: Factory = mockk("factory")
        val warehouse: Warehouse = mockk("warehouse")
        val constructor: Constructor<String> = mockk("constructor")
        val string = "test"

        every { factory.creationPattern } returns CreationPattern.SINGLETON
        every { factory.constructor } returns constructor
        every { factory.instance } returns null
        every { constructor.invoke(warehouse) } returns string
        every { factory.instance = string } just runs


        assertThrows(TypeCastException::class.java) { factory.resolveInstance<String>(warehouse) }

        verifySequence {
            factory.creationPattern
            factory.instance
            factory.constructor
            factory.instance = string
            factory.instance
        }
    }

    @Test
    fun `Test retrieving a singleton instance`() {
        val factory: Factory = mockk("factory")
        val warehouse: Warehouse = mockk("warehouse")
        val constructor: Constructor<String> = mockk("constructor")
        val string = "test"

        every { factory.creationPattern } returns CreationPattern.SINGLETON
        every { factory.constructor } returns constructor
        every { factory.instance } returns string
        every { constructor.invoke(warehouse) } returns string
        every { factory.instance = string } just runs


        Assertions.assertEquals(string, factory.resolveInstance<String>(warehouse))

        verifySequence {
            factory.creationPattern
            factory.instance
            factory.instance
        }
    }
}
