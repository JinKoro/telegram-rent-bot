import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import io.gitlab.arturbosch.detekt.Detekt as Detekt

group = "telegram.rent.bot"
version = "1.0"

plugins {
    kotlin("jvm") version deps.versions.kotlin
    java
    application
    alias(deps.plugins.detekt)
    alias(deps.plugins.kotlin.serialization)
    alias(deps.plugins.update.dependencies)
}

buildscript {
    repositories {
        maven("https://plugins.gradle.org/m2/")
    }

    dependencies {
        classpath(deps.update.dependencies)
    }
}

application {
    mainClass.set("telegram.rent.bot.entrypoint.MainKt")
}

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

dependencies {
    implementation(deps.guice)
    implementation(deps.hoplite)
    implementation(deps.jitpack)
    implementation(deps.java.json)
    implementation(deps.bundles.ktor)
    implementation(deps.telegram.bot)
}

detekt {
    toolVersion = deps.versions.detekt.get()
    ignoreFailures = false
    parallel = true
    allRules = false
    config = files("detekt.yml")
    buildUponDefaultConfig = true
}

tasks.withType<Detekt>().configureEach {
    reports.xml.required.set(true)
    reports.html.required.set(false)
    reports.txt.required.set(false)
    reports.sarif.required.set(false)
}

tasks.withType<DependencyUpdatesTask> {
    outputFormatter = "xml"
    outputDir = "build/reports/dependencyUpdates"
    reportfileName = "dependencies"
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "15"
    kotlinOptions.freeCompilerArgs = listOf(
        "-Xopt-in=kotlinx.serialization.ExperimentalSerializationApi"
    )
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "///MainKt"
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
}
