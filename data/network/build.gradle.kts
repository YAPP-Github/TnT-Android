import co.kr.tnt.setNamespace

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
}

dependencies {
    implementation(projects.domain)

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.immutable)
}
