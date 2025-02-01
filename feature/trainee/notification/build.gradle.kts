import co.kr.tnt.setNamespace

plugins {
    id("tnt.android.feature")
}

android {
    setNamespace("feature.notification")
}

dependencies {
    implementation(libs.kotlinx.immutable)
}
