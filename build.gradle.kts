import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.intellij") version "0.6.5"
    kotlin("jvm") version "1.4.30"
}

group = "org.example"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version = "203.7148.57"
    pluginName = "log-extracts"
    setPlugins("Jetbrains TeamCity Plugin:2020.2.85695")
    updateSinceUntilBuild = true
}

tasks.withType<org.jetbrains.intellij.tasks.PatchPluginXmlTask> {
    changeNotes(
        """
        Show issue links found anywhere in commit messages in a separate column in the VCS log
        """.trimIndent()
    )

    setSinceBuild("203.0")
    setUntilBuild("230.*")
}

allprojects {
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjvm-default=enable")
            jvmTarget = "1.8"
        }
    }
}