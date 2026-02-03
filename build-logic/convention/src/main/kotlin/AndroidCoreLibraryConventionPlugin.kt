import com.android.build.gradle.LibraryExtension
import org.fasheep.fair.libs
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
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
                compileSdk = 36
                defaultConfig.targetSdk = 36
                defaultConfig.minSdk = 24

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_11
                    targetCompatibility = JavaVersion.VERSION_11
                }

                testOptions.animationsDisabled = true
            }

            tasks.withType<KotlinCompile>().configureEach {
                compilerOptions {
                    // Set JVM target to 11
                    jvmTarget.set(JvmTarget.JVM_11)
                    // Treat all Kotlin warnings as errors (disabled by default)
                    // Override by setting warningsAsErrors=true in your ~/.gradle/gradle.properties
                    val warningsAsErrors: String? by project
                    allWarningsAsErrors.set(warningsAsErrors.toBoolean())
                    freeCompilerArgs.set(
                        freeCompilerArgs.get() + listOf(
                            // Enable experimental coroutines APIs, including Flow
                            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                        )
                    )
                }
            }

            dependencies {
                "implementation"("androidx.room:room-runtime:2.6.1")
                "kapt"("androidx.room:room-compiler:2.6.1")
                "implementation"("androidx.room:room-ktx:2.6.1")
                "testImplementation"(libs.findLibrary("kotlin-test").get())
            }
        }
    }
}
