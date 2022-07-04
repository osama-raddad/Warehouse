package dsl.api.warehouse

import core.warehouse.entity.Accessibility
import core.warehouse.entity.MutableRegistry
import core.warehouse.entity.Warehouse
import dsl.api.injector.DependencyResolver
import dsl.api.injector.DependencyRetriever
import dsl.api.injector.Injector
import dsl.api.injector.InjectorFactory
import storage.StorageDB


class InjectableWarehouse(
    accessibility: Accessibility? = null,
    accessibleTo: Any? = null,
    dependencyRegistry: MutableRegistry = StorageDB(),
    injectorFactory: InjectorFactory = InjectorFactory()
) : Warehouse(accessibility, accessibleTo, dependencyRegistry), () -> Injector {
    private val dependencyResolver: DependencyResolver = DependencyResolver(this)
    private val dependencyRetriever: DependencyRetriever = DependencyRetriever(this, dependencyResolver)

    private val injector: Injector = injectorFactory(dependencyResolver, dependencyRetriever)

    override fun invoke(): Injector = injector
}