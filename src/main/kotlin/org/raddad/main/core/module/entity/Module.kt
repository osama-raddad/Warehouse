package core.module.entity

import storage.StorageDB

data class Module(
    @PublishedApi
    internal val factoryRegistry: MutableRegistry = StorageDB()
)
