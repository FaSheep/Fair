import com.android.build.gradle.LibraryExtension
import org.fasheep.fair.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            extensions.configure<LibraryExtension> {
                buildFeatures {
                    compose = true
                }
            }

            dependencies {
                "implementation"(libs.findLibrary("androidx-ui").get())
                "implementation"(libs.findLibrary("androidx-ui-graphics").get())
                "implementation"(libs.findLibrary("androidx-ui-tooling").get())
                "implementation"(libs.findLibrary("androidx-ui-tooling-preview").get())
                "implementation"(libs.findLibrary("androidx-material3").get())
                "implementation"(libs.findLibrary("androidx-material-icons-core").get())
                "implementation"(libs.findLibrary("androidx-activity-compose").get())
                "implementation"(platform(libs.findLibrary("androidx-compose-bom").get()))
                "implementation"(libs.findLibrary("androidx-compose-navigation").get())
                "implementation"(libs.findLibrary("androidx-lifecycle-viewmodel-compose").get())
                "implementation"(libs.findLibrary("androidx-lifecycle-extensions").get())
                "implementation"(libs.findLibrary("androidx-lifecycle-runtime-compose").get())
                "implementation"(libs.findLibrary("androidx-hilt-navigation-compose").get())
            }
        }
    }
}
