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
    ":feature:login",
    ":feature:roleselect",
    ":feature:webview",
)

include(
    ":feature:trainer:signup",
    ":feature:trainer:connect",
    ":feature:trainer:invite",
    ":feature:trainer:main",
    ":feature:trainer:home",
    ":feature:trainer:feedback",
    ":feature:trainer:members",
    ":feature:trainer:mypage",
    ":feature:trainer:notification",
    ":feature:trainer:addptsession",
    ":feature:trainer:modifymyinfo",
)

include(
    ":feature:trainee:signup",
    ":feature:trainee:connect",
    ":feature:trainee:main",
    ":feature:trainee:home",
    ":feature:trainee:mypage",
    ":feature:trainee:notification",
    ":feature:trainee:mealrecord",
    ":feature:trainee:mealdetail",
)
