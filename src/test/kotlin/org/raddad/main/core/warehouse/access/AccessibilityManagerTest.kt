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

package org.raddad.main.core.warehouse.access

import org.raddad.main.core.dependency.entity.Factory
import org.raddad.main.core.dependency.entity.Metadata
import org.raddad.main.core.warehouse.entity.Accessibility
import org.raddad.main.core.warehouse.entity.Warehouse
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifySequence
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.raddad.main.storage.StorageDB
import java.lang.IllegalStateException
import kotlin.reflect.KClass
import kotlin.reflect.KVisibility

internal class AccessibilityManagerTest {
    private val subject = AccessibilityManager()

    private val myWarehouse: Warehouse = mockk("my")
    private val hisWarehouse: Warehouse = mockk("his")

    @Test
    fun `resolve OPEN Warehouse Access with one original PUBLIC dependency`() {
        val metadata: Metadata = mockk("metadata")
        val factory: Factory = mockk("factory")
        val kClass: KClass<String> = mockk()


        val fakeReg = StorageDB<Metadata, Factory>()
        fakeReg[metadata] = factory

        every { hisWarehouse.accessibility } returns Accessibility.OPEN
        every { hisWarehouse.dependencyRegistry } returns fakeReg
        every { metadata.classType } returns kClass
        every { metadata.classVisibility } returns KVisibility.PUBLIC
        every { metadata.isClosed } returns false
        every { kClass.visibility } returns KVisibility.PUBLIC

        val outcome = subject.resolveWarehouseAccess(myWarehouse, hisWarehouse)

        verifySequence {
            metadata.hashCode() // retrieve entries from the included Warehouse
            hisWarehouse.accessibility // check if Warehouse has accessibility enum in this case Accessibility.OPEN
            hisWarehouse.accessibility // switch between accessibility types
            hisWarehouse.dependencyRegistry // iterate over the included warehouse registry
            metadata.classVisibility // retrieve PUBLIC dependencies
            metadata.isClosed // check if this dependency is original of the included Warehouse
            metadata.hashCode() // move dependency
        }

        Assertions.assertSame(factory, outcome[metadata])
    }

    @Test
    fun `resolve TYPED Warehouse Access with one original PUBLIC dependency`() {
        val metadata: Metadata = mockk("metadata")
        val factory: Factory = mockk("factory")
        val kClass: KClass<String> = mockk()


        val fakeReg = StorageDB<Metadata, Factory>()
        fakeReg[metadata] = factory

        every { hisWarehouse.accessibility } returns null
        every { hisWarehouse.accessibleTo } returns String
        every { myWarehouse.accessibleTo } returns String
        every { hisWarehouse.dependencyRegistry } returns fakeReg
        every { metadata.classType } returns kClass
        every { metadata.classVisibility } returns KVisibility.PUBLIC
        every { metadata.isClosed } returns false
        every { kClass.visibility } returns KVisibility.PUBLIC

        val outcome = subject.resolveWarehouseAccess(myWarehouse, hisWarehouse)

        verifySequence {
            metadata.hashCode() // retrieve entries from the included Warehouse
            hisWarehouse.accessibility // check if Warehouse has accessibility enum in this case Accessibility.OPEN
            hisWarehouse.accessibleTo
            hisWarehouse.accessibleTo
            myWarehouse.accessibleTo
            hisWarehouse.dependencyRegistry // iterate over the included warehouse registry
            metadata.classVisibility // retrieve PUBLIC dependencies
            metadata.isClosed // check if this dependency is original of the included Warehouse
            metadata.hashCode() // move dependency
        }

        Assertions.assertSame(factory, outcome[metadata])
    }


    @Test
    fun `resolve OPEN Warehouse Access with one original INTERNAL dependency`() {
        val metadata: Metadata = mockk("metadata")
        val factory: Factory = mockk("factory")
        val kClass: KClass<String> = mockk()


        val fakeReg = StorageDB<Metadata, Factory>()
        fakeReg[metadata] = factory

        every { hisWarehouse.accessibility } returns Accessibility.OPEN
        every { hisWarehouse.dependencyRegistry } returns fakeReg
        every { metadata.classType } returns kClass
        every { metadata.classVisibility } returns KVisibility.INTERNAL
        every { metadata.isClosed } returns false

        val outcome = subject.resolveWarehouseAccess(myWarehouse, hisWarehouse)

        verifySequence {
            metadata.hashCode() // retrieve entries from the included Warehouse
            hisWarehouse.accessibility // check if Warehouse has accessibility enum in this case Accessibility.OPEN
            hisWarehouse.accessibility // switch between accessibility types
            hisWarehouse.dependencyRegistry // iterate over the included warehouse registry
            metadata.classVisibility // retrieve PUBLIC dependencies none are there
        }

        Assertions.assertSame(null, outcome[metadata])
    }

    @Test
    fun `resolve LOCAL Warehouse Access with one original PUBLIC dependency`() {
        val metadata: Metadata = mockk("metadata")
        val factory: Factory = mockk("factory")
        val kClass: KClass<String> = mockk("kClass")


        val fakeReg = StorageDB<Metadata, Factory>()
        fakeReg[metadata] = factory

        every { hisWarehouse.accessibility } returns Accessibility.LOCAL
        every { hisWarehouse.dependencyRegistry } returns fakeReg
        every { metadata.classType } returns kClass
        every { metadata.className } returns null
        every { metadata.classVisibility } returns KVisibility.PUBLIC
        every { kClass.visibility } returns KVisibility.PUBLIC
        every { metadata.isClosed } returns false

        val outcome = subject.resolveWarehouseAccess(myWarehouse, hisWarehouse)

        verifySequence {
            metadata.hashCode() // retrieve entries from the included Warehouse
            hisWarehouse.accessibility // check if Warehouse has accessibility enum in this case Accessibility.OPEN
            hisWarehouse.accessibility // switch between accessibility types
            hisWarehouse.dependencyRegistry // iterate over the included warehouse registry
            metadata.classVisibility // retrieve PUBLIC dependencies
            metadata.isClosed // check if this dependency is original of the included Warehouse
            metadata.hashCode() //
            metadata.classType  // create new metadata object
            metadata.className  //
            kClass.visibility
            kClass.hashCode() //
        }
        val outcomeMetadata = Metadata(kClass, null, KVisibility.PUBLIC)
        outcomeMetadata.isClosed = true //
        Assertions.assertSame(factory, outcome[outcomeMetadata])
    }

    @Test
    fun `resolve unprovided Warehouse Access`() {
        every { hisWarehouse.accessibility } returns null
        every { hisWarehouse.accessibleTo } returns null
        every { myWarehouse.accessibleTo } returns String

        Assertions.assertThrows(IllegalStateException::class.java) {
            subject.resolveWarehouseAccess(myWarehouse, hisWarehouse)
        }
        verifySequence {
            hisWarehouse.accessibility
            hisWarehouse.accessibleTo
            hisWarehouse.accessibleTo
            myWarehouse.accessibleTo
        }
    }

    @Test
    fun `resolve ISOLATED Warehouse Access`() {
        every { hisWarehouse.accessibility } returns Accessibility.ISOLATED

        Assertions.assertThrows(IllegalStateException::class.java) {
            subject.resolveWarehouseAccess(myWarehouse, hisWarehouse)
        }

        verifySequence {
            hisWarehouse.accessibility // check if Warehouse has accessibility enum in this case Accessibility.OPEN
            hisWarehouse.accessibility // switch between accessibility types //
            hisWarehouse.accessibility
        }
    }
}
