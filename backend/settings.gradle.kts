pluginManagement {
    repositories {
        mavenCentral()
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }

    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "com.diffplug.spotless") {
                useModule("com.diffplug.spotless:spotless-plugin-gradle:${requested.version}")
            }
            if (requested.id.id == "com.adarshr.test-logger") {
                useModule("com.adarshr:gradle-test-logger-plugin:${requested.version}")
            }
        }
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

buildCache {
    local {
        directory = File(rootDir, "build-cache")
    }
}

rootProject.name = "flashcii-cards"
include(":domain")
include(":application")
include(":infrastructure")
include(":spring-shared")
include(":web")
