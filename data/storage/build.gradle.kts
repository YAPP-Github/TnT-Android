import co.kr.tnt.setNamespace

plugins {
    id("tnt.android.library")
    id("tnt.android.hilt")
}

android {
    setNamespace("data.storage")
}

dependencies {
    implementation(projects.domain)
}
