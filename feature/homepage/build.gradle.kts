plugins {
    alias(libs.plugins.fair.android.library)
    alias(libs.plugins.fair.android.library.compose)
    alias(libs.plugins.fair.android.hilt)
    alias(libs.plugins.serialization)
}

android {
    namespace = "org.fasheep.fair.feature.homepage"
}

dependencies {
    implementation(project(":core:data"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.10.0")
}
