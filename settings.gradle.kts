rootProject.name = "adobo-morphe-patches"

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/MorpheApp/registry")
            credentials {
                username = providers.gradleProperty("gpr.user").getOrElse(System.getenv("GITHUB_ACTOR"))
                password = providers.gradleProperty("gpr.key").getOrElse(System.getenv("GITHUB_TOKEN"))
            }
        }
        maven("https://jitpack.io")
    }
}

plugins {
    id("app.morphe.patches") version "1.1.1"
}

settings {
    extensions {
        defaultNamespace = "dev.jkcarino.extension"

        proguardFiles(
            rootProject
                .projectDir
                .resolve("extensions/proguard-rules.pro")
                .toString()
        )
    }
}
