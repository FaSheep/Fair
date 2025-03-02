plugins {
    alias(libs.plugins.fair.android.library)
    alias(libs.plugins.fair.android.library.compose)
    alias(libs.plugins.fair.android.hilt)
}

android {
    namespace = "org.fasheep.fair.feature.vote"
}

dependencies {
    implementation(project(":core:blockchain"))
    implementation(project(":core:network"))
    implementation(project(":core:data"))
    implementation(libs.zxing.android.embedded)
}
