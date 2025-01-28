plugins {
    id("tnt.kotlin.library")
    id("tnt.kotlin.hilt")
}

dependencies {
    implementation(libs.inject)
    implementation(libs.okhttp.logging)
    implementation(libs.coroutines.core)
}
