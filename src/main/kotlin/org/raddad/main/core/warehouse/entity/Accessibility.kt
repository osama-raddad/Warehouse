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

package core.warehouse.entity

/**
 * Accessibility enum is responsible for determining warehouse accessibility
 *
 */
enum class Accessibility {
    /**
     * Open allows any warehouse to take over this warehouse public dependencies
     * that mean that any warehouse that includes an open warehouse
     * will be able to inject his dependencies and pass them to the next warehouse
     */
    OPEN,

    /**
     * Isolated doesn't allow any warehouse to use the included public dependencies or pass them to any warehouse
     *
     */
    ISOLATED,

    /**
     * Local allow just the next warehouse to included public dependencies of this warehouse and that warehouse
     * can not pass these dependencies to anyone else
     */
    LOCAL
}
