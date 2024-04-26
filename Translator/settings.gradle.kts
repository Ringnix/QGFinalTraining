pluginManagement {
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
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
    versionCatalogs {
        create("libs") {
            // 定义版本
            version("okhttp", "3.10.0")
            version("niceSpinner", "1.4.3")
            version("cardview", "1.0.0")
            version("gsonConverter", "2.4.0")
version("bottomNavigation","2.0.4")
            // 定义依赖项
            library("okhttp", "com.squareup.okhttp3", "okhttp").versionRef("okhttp")
            library("niceSpinner", "com.github.arcadefire", "nice-spinner").versionRef("niceSpinner")
            library("cardview", "androidx.cardview", "cardview").versionRef("cardview")
            library("gsonConverter", "com.squareup.retrofit2", "converter-gson").versionRef("gsonConverter")
            library("bottomNavigation", "com.ashokvarma.android:bottom-navigation-bar:2.0.4")
           

        }
    }
}

rootProject.name = "Translator"
include(":app")
