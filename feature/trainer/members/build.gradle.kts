import co.kr.tnt.setNamespace

plugins {
    id("tnt.android.feature")
}

android {
    setNamespace("feature.trainer.members")
}

dependencies {
    implementation(libs.kotlinx.immutable)
}
