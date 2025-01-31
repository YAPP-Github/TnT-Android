import co.kr.tnt.setNamespace

plugins {
    id("tnt.android.feature")
}

android {
    setNamespace("feature.trainer.webview")
}

dependencies {
    implementation(libs.kotlinx.immutable)
}
