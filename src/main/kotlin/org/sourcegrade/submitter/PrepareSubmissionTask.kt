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

import org.gradle.api.GradleException
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.getByType

@Suppress("LeakingThis")
abstract class PrepareSubmissionTask : Jar() {

    @get:Input
    abstract val assignmentId: Property<String>

    @get:Input
    abstract val studentId: Property<String>

    @get:Input
    abstract val firstName: Property<String>

    @get:Input
    abstract val lastName: Property<String>

    @get:InputFile
    val submissionInfoFile = project.buildDir.resolve("resources/submit/submission-info.json")

    init {
        dependsOn("writeSubmissionInfo")
        group = "submit"
        from(*project.extensions.getByType<SourceSetContainer>().map { it.allSource }.toTypedArray())
        from(submissionInfoFile)
        archiveFileName.set(
            assignmentId.zip(lastName) { assignmentId, lastName ->
                "$assignmentId-$lastName"
            }.zip(firstName) { left, firstName ->
                "$left-$firstName-submission"
            }.zip(archiveExtension) { left, extension ->
                "$left.$extension"
            }
        )
        setOnlyIf {
            verifySubmit()
            true
        }
    }

    private fun verifySubmit() {
        val errors = buildString {
            if (!assignmentId.isPresent) appendLine("assignmentId")
            if (!studentId.isPresent) appendLine("studentId")
            if (!firstName.isPresent) appendLine("firstName")
            if (!lastName.isPresent) appendLine("lastName")
        }
        if (errors.isNotEmpty()) {
            throw GradleException(
                """
There were some errors preparing your submission. The following required properties were not set:
$errors
"""
            )
        }
    }
}
