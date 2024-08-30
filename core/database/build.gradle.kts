plugins {
    alias(libs.plugins.fair.android.library.core)
    alias(libs.plugins.fair.android.hilt)
}

android {
    namespace = "org.fasheep.fair.core.database"
}

dependencies {
    api(project(":core:model"))
}
