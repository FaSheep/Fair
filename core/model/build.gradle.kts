import org.jetbrains.kotlin.gradle.dsl.JvmTarget


plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

kotlin {
    compilerOptions.jvmTarget = JvmTarget.JVM_11
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
