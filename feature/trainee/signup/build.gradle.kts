import co.kr.tnt.setNamespace

plugins {
    id("tnt.android.feature")
}

android {
    setNamespace("feature.trainee.signup")
}

dependencies {
    implementation(libs.kotlinx.immutable)
}
