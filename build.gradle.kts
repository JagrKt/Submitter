import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("DSL_SCOPE_VIOLATION") // https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    `java-gradle-plugin`
    alias(libs.plugins.gradle.publish)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

group = "org.sourcegrade"
version = "0.5.2-SNAPSHOT"

val kotlinxSerializationVersion: String by project

dependencies {
    implementation(gradleKotlinDsl())
    implementation(libs.annotations)
    implementation(libs.serialization)
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }
}

gradlePlugin {
    plugins {
        create("submitter") {
            id = "org.sourcegrade.submitter"
            displayName = "Jagr Submitter"
            description = "Gradle plugin for submitting source code for the Jagr AutoGrader"
            implementationClass = "org.sourcegrade.submitter.SubmitterPlugin"
        }
    }
}

pluginBundle {
    website = "https://www.sourcegrade.org"
    vcsUrl = "https://github.com/SourceGrade/Submitter"
    tags = listOf("jagr", "assignment", "submission", "grading")
}
