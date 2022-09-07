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
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.getByType

@Suppress("LeakingThis")
abstract class PrepareSubmissionTask : Jar() {

    @get:Input
    val submitExtension: SubmitExtension = project.extensions.getByType()

    @get:InputFile
    val submissionInfoFile = project.buildDir.resolve("resources/submit/submission-info.json")

    init {
        dependsOn("writeSubmissionInfo")
        group = "submit"
        from(*project.extensions.getByType<SourceSetContainer>().map { it.allSource }.toTypedArray())
        from(submissionInfoFile)
        archiveFileName.set(
            buildString {
                append(submitExtension.assignmentId, "-")
                append(submitExtension.lastName, "-")
                append(submitExtension.firstName, "-")
                append("submission.", submitExtension.archiveExtension ?: "jar")
            }
        )
        setOnlyIf {
            verifySubmit()
            true
        }
    }

    private fun verifySubmit() {
        val errors = buildString {
            if (submitExtension.assignmentId == null) appendLine("assignmentId")
            if (submitExtension.studentId == null) appendLine("studentId")
            if (submitExtension.firstName == null) appendLine("firstName")
            if (submitExtension.lastName == null) appendLine("lastName")
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
