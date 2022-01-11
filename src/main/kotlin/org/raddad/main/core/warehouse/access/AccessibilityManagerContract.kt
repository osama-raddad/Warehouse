package core.warehouse.access

import core.dependency.entity.Factory
import core.dependency.entity.Metadata
import core.warehouse.entity.Warehouse

interface AccessibilityManagerContract {
    fun resolveWarehouseAccess(
        myWarehouse: Warehouse,
        hisWarehouse: Warehouse
    ): Map<out Metadata, Factory>

}
