import co.kr.tnt.setNamespace

plugins {
    id("tnt.android.feature")
}

android {
    setNamespace("feature.main")
}

dependencies {
    implementation(projects.feature.home)
    implementation(projects.feature.webview)
    implementation(projects.feature.login)
    implementation(projects.feature.roleselect)
    implementation(projects.feature.trainer.main)
    implementation(projects.feature.trainee.main)
    implementation(projects.feature.trainee.signup)
    implementation(projects.feature.trainer.signup)
    implementation(projects.feature.trainer.connect)
    implementation(projects.feature.trainee.connect)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.kotlinx.immutable)
    implementation(libs.androidx.core.splashscreen)
    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.hilt.android.compiler)
}
