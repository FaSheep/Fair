plugins {
    alias(libs.plugins.fair.android.library.core)
    alias(libs.plugins.fair.android.hilt)
    alias(libs.plugins.apollographql.apollo)
}

android {
    namespace = "org.fasheep.fair.core.network"
}

apollo {
    service("service1") {
        val srcDir = "src/main/graphql/service1"
        packageName.set("org.fasheep.fair.core.network.service1")
        srcDir(srcDir)
        introspection {
            endpointUrl.set("https://api.studio.thegraph.com/query/99199/random/version/latest")
            schemaFile.set(file("$srcDir/schema.graphqls"))
        }
    }
    service("service2") {
        val srcDir = "src/main/graphql/service2"
        packageName.set("org.fasheep.fair.core.network.service2")
        srcDir(srcDir)
        introspection {
            endpointUrl.set("https://api.studio.thegraph.com/query/99199/assign/version/latest")
            schemaFile.set(file("$srcDir/schema.graphqls"))
        }
    }
}

dependencies {
    api(project(":core:model"))
    implementation(libs.apollo.runtime)
}
