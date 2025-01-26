import co.kr.tnt.setNamespace

plugins {
    id("tnt.android.feature")
}

android {
    setNamespace("feature.trainer.connect")
}

dependencies {
    implementation(libs.kotlinx.immutable)
    implementation(libs.accompanist.permissions)
}
