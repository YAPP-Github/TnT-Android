import co.kr.tnt.setNamespace
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("tnt.android.library")
    id("tnt.android.hilt")
    id("kotlinx-serialization")
}

android {
    setNamespace("data.network")

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        debug {
            buildConfigField(
                "String",
                "BASE_API_URL",
                gradleLocalProperties(rootDir, providers).getProperty("DEBUG_BASE_API_URL"),
            )
        }

        release {
            buildConfigField(
                "String",
                "BASE_API_URL",
                gradleLocalProperties(rootDir, providers).getProperty("RELEASE_BASE_API_URL"),
            )
        }
    }
}

dependencies {
    implementation(projects.domain)

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.immutable)
}
