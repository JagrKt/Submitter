/*
 *   Jagr Submitter - SourceGrade.org
 *   Copyright (C) 2021-2022 Alexander Staeding
 *   Copyright (C) 2021-2022 Contributors
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.sourcegrade.submitter

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.property
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.setValue
import javax.inject.Inject

interface SubmitExtension {
    var assignmentId: String
    var studentId: String
    var firstName: String
    var lastName: String
    var archiveExtension: String
    var requireTests: Boolean
    var requirePublicTests: Boolean
}

internal abstract class SubmitExtensionImpl @Inject constructor(
    objectFactory: ObjectFactory,
) : SubmitExtension {

    // Syntax sugar until https://github.com/gradle/gradle/issues/9268 is resolved

    abstract val assignmentIdProperty: Property<String>
    abstract val studentIdProperty: Property<String>
    abstract val firstNameProperty: Property<String>
    abstract val lastNameProperty: Property<String>
    abstract val archiveExtensionProperty: Property<String>
    val requireTestsProperty: Property<Boolean> = objectFactory.property<Boolean>().convention(true)
    val requirePublicTestsProperty: Property<Boolean> = objectFactory.property<Boolean>().convention(false)

    override var assignmentId: String by assignmentIdProperty
    override var studentId: String by studentIdProperty
    override var firstName: String by firstNameProperty
    override var lastName: String by lastNameProperty
    override var archiveExtension: String by archiveExtensionProperty
    override var requireTests: Boolean by requireTestsProperty
    override var requirePublicTests: Boolean by requirePublicTestsProperty
}
