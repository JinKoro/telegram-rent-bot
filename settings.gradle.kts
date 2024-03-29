@file:Suppress("UnstableApiUsage")

rootProject.name = "telegram.rent.bot"
enableFeaturePreview("VERSION_CATALOGS")

dependencyResolutionManagement {

    versionCatalogs {
        create("deps") {
            version("logging", "2.19.0")
            version("ktor", "2.1.1")
            version("kotlin", "1.6.10")
            version("guice", "5.1.0")
            version("jitpack", "2.0")
            version("detekt", "1.19.0")
            version("hoplite", "2.6.3")
            version("test_logger", "3.0.0")
            version("java_json", "20220924")
            version("telegram_bot", "6.0.7")
            version("update_dependencies", "0.42.0")

            alias("ktor_client_cio").to("io.ktor", "ktor-client-cio").versionRef("ktor")
            alias("ktor_client_core").to("io.ktor", "ktor-client-core").versionRef("ktor")
            alias("ktor_client_encoding_jvm").to("io.ktor", "ktor-client-encoding-jvm").versionRef("ktor")
            alias("ktor_content_negotiation").to("io.ktor", "ktor-client-content-negotiation").versionRef("ktor")
            alias("ktor_serialization").to("io.ktor", "ktor-serialization-kotlinx-json").versionRef("ktor")
            alias("telegram_bot").to("io.github.kotlin-telegram-bot.kotlin-telegram-bot", "telegram").versionRef("telegram_bot")
            alias("update_dependencies").to("com.github.ben-manes", "gradle-versions-plugin").versionRef("update_dependencies")
            alias("hoplite").to("com.sksamuel.hoplite", "hoplite-core").versionRef("hoplite")
            alias("jitpack").to("com.github.jitpack", "gradle-simple").versionRef("jitpack")
            alias("guice").to("com.google.inject", "guice").versionRef("guice")
            alias("java_json").to("org.json", "json").versionRef("java_json")
            alias("logging").to("org.apache.logging.log4j", "log4j-core").versionRef("logging")
            alias("slf4j").to("org.apache.logging.log4j", "log4j-slf4j-impl").versionRef("logging")
            alias("detekt").toPluginId("io.gitlab.arturbosch.detekt").versionRef("detekt")
            alias("update_dependencies").toPluginId("com.github.ben-manes.versions").versionRef("update_dependencies")
            alias("kotlin_serialization").toPluginId("org.jetbrains.kotlin.plugin.serialization").versionRef("kotlin")

            bundle("ktor", listOf(
                "ktor_client_cio",
                "ktor_client_core",
                "ktor_serialization",
                "ktor_client_encoding_jvm",
                "ktor_content_negotiation"
            ))
        }
    }
}
