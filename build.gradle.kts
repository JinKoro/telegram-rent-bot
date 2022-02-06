import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import io.gitlab.arturbosch.detekt.Detekt as Detekt
import java.util.Properties

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
    runtimeOnly(deps.logging)
    implementation(deps.slf4j)

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

tasks.withType<JavaExec> {
    dependsOn(":loadFileConfiguration")
    doFirst {
        System.getProperties().forEach { (k, v) ->
            systemProperty(k.toString(), v.toString())
        }
    }
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "telegram.rent.bot.entrypoint.MainKt"
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
}

tasks.register("loadFileConfiguration") {
    group = "configuration"
    doLast {
        val properties = Properties()
        val localPropertiesFile = file("$projectDir/src/main/resources/local.properties")

        if (localPropertiesFile.exists()) {
            logger.lifecycle("Loading ${localPropertiesFile.path}")
            properties.load(localPropertiesFile.readText().byteInputStream())
        }

        for ((k, v) in properties.entries) {
            System.setProperty(k.toString(), v.toString())
        }
    }
}
