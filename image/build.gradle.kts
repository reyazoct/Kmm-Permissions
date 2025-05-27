import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    id("com.vanniktech.maven.publish") version "0.31.0"
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {

    // Target declarations - add or remove as needed below. These define
    // which platforms this KMP module supports.
    // See: https://kotlinlang.org/docs/multiplatform-discover-project.html#targets
    androidTarget()

    // For iOS targets, this is also where you should
    // configure native binary output. For more information, see:
    // https://kotlinlang.org/docs/multiplatform-build-native-binaries.html#build-xcframeworks

    // A step-by-step guide on how to include this library in an XCode
    // project can be found here:
    // https://developer.android.com/kotlin/multiplatform/migrate
    val xcfName = "imageKit"

    iosX64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosSimulatorArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser {
            binaries.executable()
        }
    }

    // Source set declarations.
    // Declaring a target automatically creates a source set with the same name. By default, the
    // Kotlin Gradle Plugin creates additional source sets that depend on each other, since it is
    // common to share sources between related targets.
    // See: https://kotlinlang.org/docs/multiplatform-hierarchy.html
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlin.stdlib)
            implementation(libs.kotlinx.coroutines.core)

            // Add KMP dependencies here
            implementation(compose.runtime)
            implementation(compose.ui)
            implementation(compose.material)
            compileOnly(libs.ktor.client.core)
        }

        androidMain.dependencies {
            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.activity.compose)
            implementation(libs.gms.play.services.location)
        }

        iosMain.dependencies {
            // Add iOS-specific dependencies here. This a source set created by Kotlin Gradle
            // Plugin (KGP) that each specific iOS target (e.g., iosX64) depends on as
            // part of KMP’s default source set hierarchy. Note that this source set depends
            // on common by default and will correctly pull the iOS artifacts of any
            // KMP dependencies declared in commonMain.
        }

        wasmJsMain {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
            }
        }
    }
}

android {
    compileSdk = 35
    namespace = "tech.kotlinlang.image"

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17  // ✅ Set Java to 17
        targetCompatibility = JavaVersion.VERSION_17  // ✅ Set Java to 17
    }

    kotlin {
        jvmToolchain(17) // ✅ Correct way to set Kotlin JVM target
    }

    publishing {
        singleVariant("release") {  // This enables publishing for the "release" variant
            withSourcesJar()
            withJavadocJar()
        }
    }
}

mavenPublishing {
    coordinates(
        "tech.kotlinlang",
        "image",
        libs.versions.library.version.get(),
    )

    pom {
        name.set("permission")
        description.set("A Compose Multiplatform library to request permissions for both Android and iOS.")
        inceptionYear.set("2025")
        url.set("https://kotlinlang.tech")

        licenses {
            license {
                name.set("MIT")
                url.set("https://opensource.org/licenses/MIT")
            }
        }

        developers {
            developer {
                id.set("reyazoct")
                name.set("Reyaz Ahmad")
                email.set("reyazoct@gmail.com")
            }
        }

        scm {
            url.set("https://github.com/reyazoct/Kmm-Permissions")
        }
    }

    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()
}
