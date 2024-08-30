plugins {
    alias(libs.plugins.fair.android.library.core)
    alias(libs.plugins.fair.android.hilt)
}

android {
    namespace = "org.fasheep.fair.core.data"
}

dependencies {
    api(project(":core:database"))
}
