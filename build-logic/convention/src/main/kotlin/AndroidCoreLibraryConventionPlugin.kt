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

class AndroidCoreLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("org.jetbrains.kotlin.kapt")
//                apply("com.google.devtools.ksp")
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
                add("implementation", "androidx.room:room-runtime:2.6.1")
                add("kapt", "androidx.room:room-compiler:2.6.1")
                add("implementation", "androidx.room:room-ktx:2.6.1")
            }
        }
    }
}
