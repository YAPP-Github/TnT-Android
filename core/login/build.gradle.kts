import co.kr.tnt.setNamespace
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("tnt.android.library")
}

android {
    setNamespace("core.login")

    defaultConfig {
        manifestPlaceholders["KAKAO_NATIVE_APP_KEY"] =
            gradleLocalProperties(rootDir, providers).getProperty("KAKAO_NATIVE_APP_KEY")
    }
}

dependencies {
    implementation(libs.kakao.user)
}
