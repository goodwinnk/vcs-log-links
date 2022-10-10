import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.intellij") version "1.9.0"
    kotlin("jvm") version "1.7.0"
}

group = "com.nkrasko"
version = "0.0.5"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
}

// See https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("IC-2022.1.1")
    pluginName.set("Log Links Column")

    updateSinceUntilBuild.set(false)
}

allprojects {
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjvm-default=enable")
            jvmTarget = "11"
        }
    }
}