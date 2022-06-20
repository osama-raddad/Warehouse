/*
 * Copyright (c) 2022. , Osama Raddad
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import core.dependency.entity.CreationPattern
import core.dependency.entity.Named
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

const val FIRST_NAME = "first name"
const val LAST_NAME = "last name"

const val FIRST_NAME_VALUE = "Osama"
const val LAST_NAME_VALUE = "Raddad"

val namesDI = warehouse(Accessibility.LOCAL) {
    module {
        factory {
            name { FIRST_NAME }
            constructor { FIRST_NAME_VALUE }
            creation { CreationPattern.REUSABLE }
            contract { String::class }
        }
    }

    module {
        factory {
            name { LAST_NAME }
            constructor { LAST_NAME_VALUE }
        }
    }
}

val mainDI = warehouse(Accessibility.LOCAL) {
    warehouse { namesDI }
    module {
        factory {
            name { "GoodPerson" }
            constructor { GoodPerson(param(FIRST_NAME), param(LAST_NAME)) }
            injectsIn { Demo::class }
            creation { CreationPattern.REUSABLE }
            contract { Person::class }
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
    @Named("GoodPerson")
    private val goodPerson: Person by mainDI()

    init {
        println(goodPerson.getFirstName())
        print(goodPerson.getLastName())
    }
}
