import co.kr.tnt.setNamespace

plugins {
    id("tnt.android.feature")
}

android {
    setNamespace("feature.trainer.invite")
}

dependencies {
    implementation(libs.kotlinx.immutable)
    implementation(project(":feature:trainer:connect"))
}
