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
    implementation(projects.feature.trainee.notification)
    implementation(projects.feature.trainee.mealrecord)
    implementation(projects.feature.trainee.mealdetail)

    implementation(libs.kotlinx.immutable)
}
