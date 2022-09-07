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

import kotlinx.serialization.Serializable

interface SubmitExtension {
    var assignmentId: String?
    var studentId: String?
    var firstName: String?
    var lastName: String?
    var requireTests: Boolean
    var requirePublicTests: Boolean
    var archiveExtension: String?
}

@Serializable
internal data class SubmitExtensionImpl(
    override var assignmentId: String? = null,
    override var studentId: String? = null,
    override var firstName: String? = null,
    override var lastName: String? = null,
    override var requireTests: Boolean = true,
    override var requirePublicTests: Boolean = false,
    override var archiveExtension: String? = null,
) : SubmitExtension, java.io.Serializable
