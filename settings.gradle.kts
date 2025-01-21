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
    }
}

rootProject.name = "TnT"
include(":app")
include(":data")
include(":domain")

include(
    ":core:designsystem",
    ":core:navigation",
    ":core:ui",
)

include(
    ":feature:main",
    ":feature:home",
    ":feature:signup",
    ":feature:login",
)
