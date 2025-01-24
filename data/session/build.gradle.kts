import co.kr.tnt.setNamespace

plugins {
    id("tnt.android.library")
    id("tnt.android.hilt")
}

android {
    setNamespace("data.session")
}

dependencies {
    implementation(projects.data.network)
    implementation(projects.data.storage)
}
