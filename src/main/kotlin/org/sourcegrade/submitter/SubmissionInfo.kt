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

import kotlinx.serialization.Serializable
import org.gradle.api.tasks.SourceSet

@Serializable
internal data class SubmissionInfo(
    val assignmentId: String,
    val studentId: String,
    val firstName: String,
    val lastName: String,
    val sourceSets: List<SourceSetInfo>,
)

@Serializable
internal data class SourceSetInfo(
    val name: String,
    val files: List<String>,
) : java.io.Serializable

internal fun SubmitExtension.toSubmissionInfo(
    sourceSets: List<SourceSetInfo>,
) = SubmissionInfo(
    requireNotNull(assignmentId) { "assignmentId" },
    requireNotNull(studentId) { "studentId" },
    requireNotNull(firstName) { "firstName" },
    requireNotNull(lastName) { "lastName" },
    sourceSets,
)

internal fun SourceSet.getFiles(): List<String> {
    return allSource.files.map { file ->
        allSource.srcDirs.asSequence()
            .map(file::relativeTo)
            .reduce { a, b -> if (a.path.length < b.path.length) a else b }
            .path
    }
}

internal fun SourceSet.toInfo() = SourceSetInfo(name, getFiles())
