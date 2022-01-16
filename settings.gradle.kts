@file:Suppress("UnstableApiUsage")

rootProject.name = "telegram.telegram.rent.bot"
enableFeaturePreview("VERSION_CATALOGS")

dependencyResolutionManagement {

    versionCatalogs {
        create("deps") {
            version("ktor", "1.6.7")
            version("kotlin", "1.6.10")
            version("guice", "5.0.1")
            version("jitpack", "1.1")
            version("detekt", "1.19.0")
            version("hoplite", "1.4.16")
            version("test_logger", "3.0.0")
            version("java_json", "20211205")
            version("telegram_bot", "6.0.6")
            version("update_dependencies", "0.41.0")

            alias("ktor_client_cio").to("io.ktor", "ktor-client-cio").versionRef("ktor")
            alias("ktor_client_core").to("io.ktor", "ktor-client-core").versionRef("ktor")
            alias("ktor_serialization").to("io.ktor", "ktor-serialization").versionRef("ktor")
            alias("ktor_client_encoding_jvm").to("io.ktor", "ktor-client-encoding-jvm").versionRef("ktor")
            alias("ktor_client_serialization").to("io.ktor", "ktor-client-serialization").versionRef("ktor")
            alias("telegram_bot").to("io.github.kotlin-telegram-bot.kotlin-telegram-bot", "telegram").versionRef("telegram_bot")
            alias("update_dependencies").to("com.github.ben-manes", "gradle-versions-plugin").versionRef("update_dependencies")
            alias("hoplite").to("com.sksamuel.hoplite", "hoplite-core").versionRef("hoplite")
            alias("jitpack").to("com.github.jitpack", "gradle-simple").versionRef("jitpack")
            alias("guice").to("com.google.inject", "guice").versionRef("guice")
            alias("java_json").to("org.json", "json").versionRef("java_json")

            alias("detekt").toPluginId("io.gitlab.arturbosch.detekt").versionRef("detekt")
            alias("update_dependencies").toPluginId("com.github.ben-manes.versions").versionRef("update_dependencies")
            alias("kotlin_serialization").toPluginId("org.jetbrains.kotlin.plugin.serialization").versionRef("kotlin")

            bundle("ktor", listOf(
                "ktor_client_cio",
                "ktor_client_core",
                "ktor_serialization",
                "ktor_client_encoding_jvm",
                "ktor_client_serialization"
            ))
        }
    }
}
