import co.kr.tnt.setNamespace

plugins {
    id("tnt.android.feature")
}

android {
    setNamespace("feature.login")
}

dependencies {
    implementation(projects.core.login)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging)
}
