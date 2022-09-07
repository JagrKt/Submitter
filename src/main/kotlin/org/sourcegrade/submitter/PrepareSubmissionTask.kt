/*
 *   Jagr Submitter - SourceGrade.org
 *   Copyright (C) 2021 Alexander Staeding
 *   Copyright (C) 2021 Contributors
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.sourcegrade.submitter

import org.gradle.api.GradleException
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.getByType

@Suppress("LeakingThis")
abstract class PrepareSubmissionTask : Jar() {

    @get:InputFile
    val submissionInfoFile = project.buildDir.resolve("resources/submit/submission-info.json")

    init {
        dependsOn("writeSubmissionInfo")
        group = "submit"
        val submit = project.extensions.getByType<SubmitExtension>()
        val sourceSets = project.extensions.getByName("sourceSets") as SourceSetContainer
        from(*sourceSets.map { it.allSource }.toTypedArray())
        from(submissionInfoFile)
        archiveFileName.set(
            buildString {
                append(submit.assignmentId, "-")
                append(submit.lastName, "-")
                append(submit.firstName, "-")
                append("submission.", submit.archiveExtension ?: "jar")
            }
        )
        setOnlyIf {
            verifySubmit()
            true
        }
    }

    private fun verifySubmit() {
        val submit = project.extensions.getByType<SubmitExtension>()
        val errors = buildString {
            if (submit.assignmentId == null) appendLine("assignmentId")
            if (submit.studentId == null) appendLine("studentId")
            if (submit.firstName == null) appendLine("firstName")
            if (submit.lastName == null) appendLine("lastName")
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
