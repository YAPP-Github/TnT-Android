import co.kr.tnt.setNamespace

plugins {
    id("tnt.android.library")
    id("tnt.android.compose")
}

android {
    setNamespace("core.designsystem")
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.calendar.compose)
}
