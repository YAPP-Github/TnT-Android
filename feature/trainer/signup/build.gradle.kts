import co.kr.tnt.setNamespace

plugins {
    id("tnt.android.feature")
}

android {
    setNamespace("feature.trainer.signup")
}

dependencies {
    implementation(libs.kotlinx.immutable)
}
