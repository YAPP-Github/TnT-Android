import co.kr.tnt.setNamespace

plugins {
    id("tnt.android.feature")
}

android {
    setNamespace("feature.trainer.home")
}

dependencies {
    implementation(libs.kotlinx.immutable)
    implementation(libs.calendar.compose)
}
