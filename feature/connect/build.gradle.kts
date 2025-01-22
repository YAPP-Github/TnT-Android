import co.kr.tnt.setNamespace

plugins {
    id("tnt.android.feature")
}

android {
    setNamespace("feature.connect")
}

dependencies {
    implementation(libs.kotlinx.immutable)
}
