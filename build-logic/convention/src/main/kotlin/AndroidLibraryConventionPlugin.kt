import com.android.build.gradle.LibraryExtension
import org.fasheep.fair.libs
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension> {
                compileSdk = 34
                defaultConfig.targetSdk = 34
                defaultConfig.minSdk = 24

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_11
                    targetCompatibility = JavaVersion.VERSION_11
                }

                testOptions.animationsDisabled = true
            }

            tasks.withType<KotlinCompile>().configureEach {
                kotlinOptions {
                    // Set JVM target to 11
                    jvmTarget = JavaVersion.VERSION_11.toString()
                    // Treat all Kotlin warnings as errors (disabled by default)
                    // Override by setting warningsAsErrors=true in your ~/.gradle/gradle.properties
                    val warningsAsErrors: String? by project
                    allWarningsAsErrors = warningsAsErrors.toBoolean()
                    freeCompilerArgs = freeCompilerArgs + listOf(
                        // Enable experimental coroutines APIs, including Flow
                        "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                    )
                }
            }

            dependencies {
                add("implementation", libs.findLibrary("androidx-core-ktx").get())
                add("implementation", libs.findLibrary("androidx-lifecycle-runtime-ktx").get())
            }
        }
    }
}
