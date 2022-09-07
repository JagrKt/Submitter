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

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.getByType

@Suppress("LeakingThis")
abstract class WriteSubmissionInfoTask : DefaultTask() {

    @get:Input
    val submitExtension: SubmitExtension = project.extensions.getByType()

    @get:OutputFile
    val submissionInfoFile = project.buildDir.resolve("resources/submit/submission-info.json")

    init {
        dependsOn("compileJava")
        group = "submit"
        val submit = project.extensions.getByType<SubmitExtension>()
        if (submit.requireTests) {
            project.tasks.findByName("test")?.let { dependsOn(it) }
        }
        if (submit.requirePublicTests) {
            project.tasks.findByName("publicTest")?.let { dependsOn(it) }
        }
    }

    @TaskAction
    fun runTask() {
        val submissionInfo = submitExtension.toSubmissionInfo(
            project.extensions.getByType<SourceSetContainer>().map { it.toInfo() })
        submissionInfoFile.apply {
            parentFile.mkdirs()
            writeText(Json.encodeToString(submissionInfo))
        }
    }
}
