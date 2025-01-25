import co.kr.tnt.setNamespace

plugins {
    id("tnt.android.library")
    id("tnt.android.hilt")
}

android {
    setNamespace("data.repository")
}

dependencies {
    implementation(projects.domain)
    implementation(projects.data.network)
    implementation(projects.data.storage)
}
