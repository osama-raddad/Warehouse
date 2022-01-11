package storage

import java.util.concurrent.ConcurrentHashMap

class StorageDB<K,V>: ConcurrentHashMap<K, V>()
