plugins {
    alias(libs.plugins.fair.android.library)
    alias(libs.plugins.fair.android.library.compose)
    alias(libs.plugins.fair.android.hilt)
}

android {
    namespace = "org.fasheep.fair.feature.sortition"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:blockchain"))
}
