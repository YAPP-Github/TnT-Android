import co.kr.tnt.setNamespace

plugins {
    id("tnt.android.feature")
}

android {
    setNamespace("feature.main")
}

dependencies {
    implementation(projects.feature.home)
    implementation(projects.feature.login)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.kotlinx.immutable)
    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.hilt.android.compiler)
}
