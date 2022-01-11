package dsl.api.dependency

import core.dependency.entity.CreationPattern.*
import core.dependency.entity.Factory
import core.warehouse.entity.Warehouse
import java.util.concurrent.locks.ReentrantLock

@PublishedApi
internal  val lock = ReentrantLock()
@PublishedApi
internal inline fun <reified T> Factory.resolveInstance(warehouse: Warehouse): T {
    val value = when (creationPattern) {
        NEW -> constructor(warehouse)
        SINGLETON -> {
            lock.lock()
            try {
                if (instance == null) instance = constructor(warehouse)
                instance
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                lock.unlock()
            }
        }
        REUSABLE -> {
            if (instance == null) instance = constructor(warehouse)
            instance
        }
    }
    return safeCast(value)
}
