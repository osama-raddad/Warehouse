import core.dependency.entity.CreationPattern
import core.warehouse.entity.Accessibility
import core.warehouse.entity.Warehouse
import dsl.api.dependency.factory
import dsl.api.module.module
import dsl.api.warehouse.warehouse


interface Person {
    fun getFirstName(): String
    fun getLastName(): String
}

class GoodPerson(private val firstName: String, private val lastName: String) : Person {
    override fun getFirstName() = firstName
    override fun getLastName() = lastName
}

class BadPerson(private val firstName: String, private val lastName: String) : Person {
    override fun getFirstName() = firstName
    override fun getLastName() = lastName
}

interface Car {
    fun getSpeed(): Int
    fun getOwner(): Person
}

class NiceCar(private val person: Person, private val speed: Int) : Car {
    override fun getSpeed() = speed
    override fun getOwner() = person
}


val namesDI = warehouse {

    this add module {
        this add factory {
            this name "first name"
            this constructor { "Osama" }
            this creation CreationPattern.SINGLETON
        }
    }
    this add module {
        this add factory {
            this name "a"
            this constructor { "Raddad" }
            this creation CreationPattern.SINGLETON
        }
    }
}

val mainDI = warehouse(Accessibility.OPEN) {
    this add namesDI
    this add module {
        this add factory {
            this constructor { GoodPerson(param("first name"), "yy") }
            this creation CreationPattern.SINGLETON
            this injectsIn Main::class
        }
    }
    this add module {
        this add factory {
            this name "first name"
            this constructor { "Osama" }
            this creation CreationPattern.SINGLETON
        }
    }
}

class Main {
    private val goodPerson: GoodPerson by mainDI.inject()

    init {
        print(goodPerson.getFirstName())
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Main()
        }
    }
}