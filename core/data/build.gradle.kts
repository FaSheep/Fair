plugins {
    alias(libs.plugins.fair.android.library.core)
    alias(libs.plugins.fair.android.hilt)
}

android {
    namespace = "org.fasheep.fair.core.data"
}

dependencies {
    implementation(project(":core:database"))
    implementation(project(":core:blockchain"))
    implementation(project(":core:network"))
}
