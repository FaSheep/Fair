plugins {
    alias(libs.plugins.fair.android.library.core)
    alias(libs.plugins.fair.android.hilt)
    alias(libs.plugins.apollographql.apollo)
}

android {
    namespace = "org.fasheep.fair.core.network"
}

apollo {
    service("service") {
        packageName.set("org.fasheep.fair.core.network")
    }
}

dependencies {
    api(project(":core:model"))
    implementation(libs.apollo.runtime)
}
