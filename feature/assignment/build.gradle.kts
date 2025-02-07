plugins {
    alias(libs.plugins.fair.android.library)
    alias(libs.plugins.fair.android.library.compose)
    alias(libs.plugins.fair.android.hilt)
}

android {
    namespace = "org.fasheep.fair.feature.assignment"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:blockchain"))
    implementation(project(":core:network"))
}
