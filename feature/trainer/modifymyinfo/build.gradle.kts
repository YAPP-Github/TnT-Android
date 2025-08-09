import co.kr.tnt.setNamespace

plugins {
    id("tnt.android.feature")
}

android {
    setNamespace("feature.trainer.modifymyinfo")
}

dependencies {
    implementation(libs.kotlinx.immutable)
}
