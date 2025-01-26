import co.kr.tnt.setNamespace

plugins {
    id("tnt.android.feature")
}

android {
    setNamespace("feature.login")
}

dependencies {
    implementation(projects.core.login)
}
