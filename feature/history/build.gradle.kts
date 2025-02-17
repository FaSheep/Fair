plugins {
    alias(libs.plugins.fair.android.library)
    alias(libs.plugins.fair.android.library.compose)
    alias(libs.plugins.fair.android.hilt)
    kotlin("plugin.serialization") version "1.9.0"
}

android {
    namespace = "org.fasheep.fair.feature.history"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:blockchain"))
    implementation(project(":core:network"))
    implementation(libs.kotlinx.serialization.json)
}
