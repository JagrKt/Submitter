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

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.gradle.api.DefaultTask
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.listProperty
import org.gradle.kotlin.dsl.provideDelegate

@Suppress("LeakingThis")
abstract class WriteSubmissionInfoTask : DefaultTask() {

    @get:Input
    abstract val assignmentId: Property<String>

    @get:Input
    abstract val studentId: Property<String>

    @get:Input
    abstract val firstName: Property<String>

    @get:Input
    abstract val lastName: Property<String>

    @get:Input
    abstract val requireTests: Property<Boolean>

    @get:Input
    abstract val requirePublicTests: Property<Boolean>

    @get:Input
    internal val sourceSets: ListProperty<SourceSetInfo> = project.objects.listProperty<SourceSetInfo>()
        .convention(project.extensions.getByType<SourceSetContainer>().map { it.toInfo() })

    @get:OutputFile
    val submissionInfoFile = project.buildDir.resolve("resources/submit/submission-info.json")

    init {
        dependsOn("compileJava")
        group = "submit"
        dependsOn(requireTests.map { if (it) project.tasks["test"] else emptyList<Any>() })
        dependsOn(requirePublicTests.map { if (it) project.tasks["publicTest"] else emptyList<Any>() })
    }

    @TaskAction
    fun runTask() {
        val submissionInfo = SubmissionInfo(
            assignmentId.get(),
            studentId.get(),
            firstName.get(),
            lastName.get(),
            sourceSets.get(),
        )
        submissionInfoFile.apply {
            parentFile.mkdirs()
            writeText(Json.encodeToString(submissionInfo))
        }
    }
}
