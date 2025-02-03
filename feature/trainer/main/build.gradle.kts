import co.kr.tnt.setNamespace

plugins {
    id("tnt.android.feature")
}

android {
    setNamespace("feature.trainer.main")
}

dependencies {
    implementation(projects.feature.trainer.home)

    implementation(libs.kotlinx.immutable)
}
