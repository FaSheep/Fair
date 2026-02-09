plugins {
    alias(libs.plugins.fair.android.application)
    alias(libs.plugins.fair.android.application.compose)
    alias(libs.plugins.fair.android.hilt)
}

android {
    namespace = "org.fasheep.fair"

    defaultConfig {
        applicationId = "org.fasheep.fair"
        versionCode = 4
        versionName = "1.0.0-alpha4"

        testInstrumentationRunner =
            "org.fasheep.fair.testing.TestRunner"
    }
}

dependencies {
    implementation(project(":feature:assignment"))
    implementation(project(":feature:vote"))
    implementation(project(":feature:sortition"))
    implementation(project(":feature:history"))
    implementation(libs.zxing.android.embedded)
}