import core.warehouse.entity.Accessibility
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


@Retention(AnnotationRetention.RUNTIME)
annotation class Main

val namesDI = warehouse(Accessibility.ISOLATED) {

    this add module {
        this add factory {
            this name "first name"
            this constructor { "Osama" }

        }
    }
    this add module {
        this add factory {
            this name "last name"
            this constructor { "Raddad" }

        }
    }
}

val mainDI = warehouse {
    this add namesDI
    this add module {
        this add factory {
            this constructor { GoodPerson(param("first name"), param("last name")) }
            this injectsIn Demo::class
        }
    }
}

class Demo {
    private val goodPerson: GoodPerson by mainDI.inject()

    init {
        println(goodPerson.getFirstName())
        print(goodPerson.getLastName())
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Demo()
        }
    }
}
