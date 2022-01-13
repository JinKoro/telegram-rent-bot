import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import io.gitlab.arturbosch.detekt.Detekt as Detekt

plugins {
    kotlin("jvm") version "1.6.10"

    alias(deps.plugins.detekt)
    alias(deps.plugins.kotlin.serialization)
    alias(deps.plugins.update.dependencies)
}

group = "telegram.rent.bot"
version = "1.0"

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

buildscript {
    repositories {
        maven("https://plugins.gradle.org/m2/")
    }

    dependencies {
        classpath(deps.update.dependencies)
    }
}

dependencies {
    implementation(deps.guice)
    implementation(deps.hoplite)
    implementation(deps.jitpack)
    implementation(deps.java.json)
    implementation(deps.bundles.ktor)
    implementation(deps.telegram.bot)
    implementation(deps.kotlin.serialization)
    implementation(kotlin("stdlib-jdk8"))
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
    reports {
        xml.required.set(true)
        html.required.set(false)
        txt.required.set(false)
        sarif.required.set(false)
    }
}

tasks.withType<DependencyUpdatesTask> {
    outputFormatter = "json"
    outputDir = "build/dependencyUpdates"
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "15"
        freeCompilerArgs = listOf(
            "-Xuse-experimental=io.ktor.locations.KtorExperimentalLocationsAPI",
            "-Xuse-experimental=io.ktor.util.KtorExperimentalAPI",
            "-Xuse-experimental=kotlin.ExperimentalStdlibApi",
            "-Xuse-experimental=kotlinx.serialization.InternalSerializationApi",
            "-Xuse-experimental=kotlinx.serialization.ExperimentalSerializationApi",
            "-Xuse-experimental=kotlin.time.ExperimentalTime",
            "-Xopt-in=kotlin.RequiresOptIn"
        )
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "15"
}
