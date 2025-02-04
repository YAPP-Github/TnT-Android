pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = java.net.URI("https://devrepo.kakao.com/nexus/content/groups/public/") }
    }
}

rootProject.name = "TnT"
include(":app")
include(":domain")

include(
    ":data:network",
    ":data:storage",
    ":data:repository",
    ":data:session",
)

include(
    ":core:designsystem",
    ":core:navigation",
    ":core:ui",
    ":core:login",
)

include(
    ":feature:main",
    ":feature:home",
    ":feature:login",
    ":feature:roleselect",
    ":feature:connect",
    ":feature:trainer:signup",
    ":feature:trainer:connect",
    ":feature:trainer:notification",
    ":feature:trainee:signup",
    ":feature:trainee:connect",
    ":feature:trainee:mypage",
    ":feature:trainee:notification",
    ":feature:webview",
)
