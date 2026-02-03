import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
//    alias(libs.plugins.fair.android.application)
//    alias(libs.plugins.fair.android.application.compose)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)

}

android {
    namespace = "org.fasheep.fair"

    defaultConfig {
        applicationId = "org.fasheep.fair"
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner =
            "org.fasheep.fair.testing.TestRunner"
    }
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    jvm()

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(project(":core:model"))
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
//            implementation(libs.androidx.lifecycle.viewmodelCompose)
//            implementation(libs.androidx.lifecycle.runtimeCompose)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
//            implementation(compose.desktop.currentOs)
//            implementation(libs.kotlinx.coroutinesSwing)
        }
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "org.example.project.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "org.example.project"
            packageVersion = "1.0.0"
        }
    }
}
