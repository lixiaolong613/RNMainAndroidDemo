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
    // 1️⃣ 引入 React Native 的 Gradle 插件工程
    includeBuild("../node_modules/@react-native/gradle-plugin")
}
// 2️⃣ 应用 React Native 的 settings 插件
plugins {
    id("com.facebook.react.settings")
}


dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
//        // ✅ 添加 React Native 依赖路径
//        maven {
//            url = uri("../node_modules/react-native/android")
//        }
//        maven {
//            url = uri("../node_modules/jsc-android/dist")
//        }
    }
}
// 3️⃣ 配置 ReactSettingsExtension —— 启用自动链接
extensions.configure<com.facebook.react.ReactSettingsExtension> {
    autolinkLibrariesFromCommand()
}



rootProject.name = "RnDemo"
include(":app")
// 4️⃣ （再次显式包含插件工程，兼容 Gradle 的解析阶段）
includeBuild("../node_modules/@react-native/gradle-plugin")

