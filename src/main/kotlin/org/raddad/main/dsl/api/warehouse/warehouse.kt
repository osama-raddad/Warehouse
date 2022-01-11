package dsl.api.warehouse

import core.warehouse.access.AccessibilityManager
import core.warehouse.access.AccessibilityManagerContract
import core.warehouse.entity.Accessibility

fun warehouse(
    accessibleTo: Any,
    accessibilityManager: AccessibilityManagerContract = AccessibilityManager(),
    block: WarehouseBuilder.() -> Unit,
) = WarehouseBuilder(accessibleTo = accessibleTo,accessibilityManager = accessibilityManager).apply(block).build()

fun warehouse(
    accessibility: Accessibility = Accessibility.LOCAL,
    accessibilityManager: AccessibilityManagerContract = AccessibilityManager(),
    block: WarehouseBuilder.() -> Unit,
) =  WarehouseBuilder(accessibility = accessibility,accessibilityManager = accessibilityManager).apply(block).build()
