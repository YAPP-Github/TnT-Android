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
    ":feature:trainer:signup",
    ":feature:trainer:home",
    ":feature:trainer:connect",
    ":feature:trainee:signup",
    ":feature:trainer:main",
    ":feature:trainee:home",
    ":feature:trainer:feedback",
    ":feature:trainer:members",
    ":feature:trainee:connect",
    ":feature:trainee:mypage",
    ":feature:webview",
)
