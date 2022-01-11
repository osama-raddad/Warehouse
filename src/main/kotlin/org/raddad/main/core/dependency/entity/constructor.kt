package core.dependency.entity

import core.warehouse.entity.Warehouse


typealias Constructor<T> = Warehouse.() -> T
