plugins {
    id("tnt.kotlin.library")
    id("tnt.kotlin.hilt")
}

dependencies {
    implementation(libs.inject)
    implementation(libs.coroutines.core)
}
