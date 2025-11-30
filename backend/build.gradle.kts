
plugins {
    alias(libs.plugins.gradle.test.logger.plugin)
    alias(libs.plugins.spotless.plugin)
    jacoco
    pmd
    id("com.github.spotbugs") version "5.2.0"
}

allprojects {
    group = "com.clemnjord.flashcii"
    version = "1.0.0-SNAPSHOT"


}

subprojects {
    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(21)
    }

    apply(plugin = "com.adarshr.test-logger")
    testlogger {
        theme = com.adarshr.gradle.testlogger.theme.ThemeType.MOCHA
        showCauses = true
        slowThreshold = 2000
        showSummary = true
        showSimpleNames = false
        showSkipped = true
        showStandardStreams = false
        showPassedStandardStreams = true
        showSkippedStandardStreams = true
        showFailedStandardStreams = true
    }

    apply(plugin = "com.diffplug.spotless")
    spotless {
        java {
            importOrder()
            removeUnusedImports()
            forbidWildcardImports()
            forbidModuleImports()
            palantirJavaFormat()
            formatAnnotations()
        }
    }

    afterEvaluate {
        val spotless = tasks.findByName("spotlessApply")
        if (spotless != null) {
            tasks.withType<JavaCompile>() {
                finalizedBy(spotless)
            }
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()

        maxHeapSize = "1G"

        testLogging {
            events("passed")
        }

        finalizedBy(tasks.withType<JacocoReport>())
    }

    apply(plugin = "jacoco")
    jacoco {
        reportsDirectory = layout.buildDirectory.dir("reports/jacoco").get().asFile
    }

    apply(plugin = "pmd")

    apply(plugin = "com.github.spotbugs")
    spotbugs {
        effort.set(com.github.spotbugs.snom.Effort.MAX)
    }

    tasks.withType<com.github.spotbugs.snom.SpotBugsTask>().configureEach {
        dependsOn(tasks.named("processResources"))
    }

    // Exclude test code from SpotBugs analysis
    tasks.withType<com.github.spotbugs.snom.SpotBugsTask>() {
        enabled = false
    }
}

