import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

private val kakaoNativeAppKey: String =
    gradleLocalProperties(rootDir, providers).getProperty("KAKAO_NATIVE_APP_KEY")

plugins {
    id("tnt.android.application")
    id("tnt.android.compose")
}

android {
    namespace = "co.kr.tnt"
    compileSdk = 35

    defaultConfig {
        applicationId = "co.kr.tnt"
        minSdk = 27
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "KAKAO_NATIVE_APP_KEY", "\"${kakaoNativeAppKey}\"")
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }

        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.feature.main)
    implementation(projects.domain)
    implementation(projects.data.network)
    implementation(projects.data.storage)
    implementation(projects.data.repository)
    implementation(projects.data.session)

    implementation(libs.androidx.activity.compose)
    implementation(libs.kakao.user)
}
