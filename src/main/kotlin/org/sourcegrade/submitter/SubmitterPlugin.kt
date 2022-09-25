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

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.typeOf

class SubmitterPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val submitExtension = target.extensions.create(
            typeOf<SubmitExtension>(),
            "submit",
            SubmitExtensionImpl::class.java,
        ) as SubmitExtensionImpl
        target.tasks.register<PrepareSubmissionTask>("prepareSubmission") {
            assignmentId.set(submitExtension.assignmentIdProperty)
            studentId.set(submitExtension.studentIdProperty)
            firstName.set(submitExtension.firstNameProperty)
            lastName.set(submitExtension.lastNameProperty)
            archiveExtension.set(submitExtension.archiveExtensionProperty)
        }
        target.tasks.register<WriteSubmissionInfoTask>("writeSubmissionInfo") {
            assignmentId.set(submitExtension.assignmentIdProperty)
            studentId.set(submitExtension.studentIdProperty)
            firstName.set(submitExtension.firstNameProperty)
            lastName.set(submitExtension.lastNameProperty)
            requireTests.set(submitExtension.requireTests)
            requirePublicTests.set(submitExtension.requirePublicTests)
        }
    }
}
