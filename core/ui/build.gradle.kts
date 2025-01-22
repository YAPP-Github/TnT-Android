import co.kr.tnt.setNamespace

plugins {
    id("tnt.android.library")
    id("tnt.android.compose")
    alias(libs.plugins.kotlin.serialization)
}

android {
    setNamespace("core.ui")
}

dependencies {
    implementation(projects.core.designsystem)

    implementation(libs.accompanist.permissions)
}
