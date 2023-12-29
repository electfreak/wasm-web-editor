pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

//plugins {
//    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
//}

rootProject.name = "editor"

includeBuild("editor-frontend")
includeBuild("editor-backend")