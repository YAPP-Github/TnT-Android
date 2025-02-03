import co.kr.tnt.setNamespace

plugins {
    id("tnt.android.feature")
}

android {
    setNamespace("feature.trainer.main")
}

dependencies {
    implementation(projects.feature.trainer.home)
    implementation(projects.feature.trainer.feedback)
    implementation(projects.feature.trainer.members)
    implementation(projects.feature.trainer.mypage)

    implementation(libs.kotlinx.immutable)
}
