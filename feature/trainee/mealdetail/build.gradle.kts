import co.kr.tnt.setNamespace

plugins {
    id("tnt.android.feature")
}

android {
    setNamespace("feature.trainee.mealdetail")
}

dependencies {
    implementation(libs.kotlinx.immutable)
    implementation(libs.calendar.compose)
}
