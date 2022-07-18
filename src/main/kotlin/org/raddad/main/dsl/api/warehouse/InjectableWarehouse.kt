package org.raddad.main.dsl.api.warehouse

import org.raddad.main.core.warehouse.entity.Accessibility
import org.raddad.main.core.warehouse.entity.MutableRegistry
import org.raddad.main.core.warehouse.entity.Warehouse
import org.raddad.main.dsl.api.injector.DependencyResolver
import org.raddad.main.dsl.api.injector.DependencyRetriever
import org.raddad.main.dsl.api.injector.Injector
import org.raddad.main.dsl.api.injector.InjectorFactory
import org.raddad.main.storage.StorageDB


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
