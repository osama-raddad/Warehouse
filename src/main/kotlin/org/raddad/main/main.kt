import core.warehouse.entity.Accessibility
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

//
//@Retention(AnnotationRetention.RUNTIME)
//annotation class Main

const val FIRST_NAME = "first name"
const val LAST_NAME = "last name"

const val FIRST_NAME_VALUE = "Osama"
const val LAST_NAME_VALUE = "Raddad"

val namesDI = warehouse(Accessibility.LOCAL) {
    module {
        factory {
             name {FIRST_NAME}
             constructor { FIRST_NAME_VALUE }
        }
    }

    module {
        factory {
            name { LAST_NAME }
            constructor { LAST_NAME_VALUE }
        }
    }
}

val mainDI = warehouse {
    warehouse { namesDI }
    module {
        factory {
            constructor { GoodPerson(param(FIRST_NAME), param(LAST_NAME)) }
            injectsIn { Demo::class }
        }
    }
}

object Run {
    @JvmStatic
    fun main(args: Array<String>) {
        Demo()
    }
}

class Demo {
    private val goodPerson: GoodPerson by mainDI()

    init {
        println(goodPerson.getFirstName())
        print(goodPerson.getLastName())
    }
}
