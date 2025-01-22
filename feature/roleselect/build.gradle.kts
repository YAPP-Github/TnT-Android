import co.kr.tnt.setNamespace

plugins {
    id("tnt.android.feature")
}

android {
    setNamespace("feature.roleselect")
}

dependencies {
    implementation(libs.kotlinx.immutable)
}
