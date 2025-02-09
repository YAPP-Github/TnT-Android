import co.kr.tnt.setNamespace

plugins {
    id("tnt.android.feature")
}

android {
    setNamespace("feature.trainer.mypage")
}

dependencies {
    implementation(projects.core.login)

    implementation(libs.kotlinx.immutable)
}
