plugins {
    `java-library`
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":application"))
    implementation(project(":infrastructure"))

    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.aspects)
    implementation(libs.aspectj)
}