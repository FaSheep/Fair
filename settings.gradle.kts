pluginManagement {
    repositories {
        includeBuild("build-logic")
        maven { url = uri("https://maven.aliyun.com/repository/public/") }
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
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven { url = uri("https://maven.aliyun.com/repository/public/") }
        google()
        mavenCentral()
    }
}

rootProject.name = "Fair"
include(":app")
include(":composeApp")
include(":feature:assignment")
include(":feature:vote")
include(":feature:sortition")
include(":feature:history")
include(":core:data")
include(":core:model")
include(":core:blockchain")
include(":core:network")
