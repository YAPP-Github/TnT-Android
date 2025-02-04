import co.kr.tnt.setNamespace

plugins {
    id("tnt.android.feature")
}

android {
    setNamespace("feature.trainee.main")
}

dependencies {
    implementation(projects.feature.trainee.home)
    implementation(projects.feature.trainee.mypage)

    implementation(libs.kotlinx.immutable)
}
