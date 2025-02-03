import co.kr.tnt.setNamespace

plugins {
    id("tnt.android.feature")
}

android {
    setNamespace("feature.trainee.main")
}

dependencies {
    implementation(libs.kotlinx.immutable)
}
