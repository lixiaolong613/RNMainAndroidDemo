import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.facebook.react")
}

android {
    namespace = "com.example.rndemo"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.rndemo"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    flavorDimensions += "variant"
    productFlavors {
        create("default") {
            dimension = "variant"
            applicationId = "com.example.rndemo"
            versionName = "1.0"
            manifestPlaceholders["appName"] = "RnDemo"
            manifestPlaceholders["appIcon"] = "@mipmap/ic_launcher"
        }
        create("gpay") {
            dimension = "variant"
            applicationId = "com.example.rndemo.gpay"
            versionName = "1.0-gpay"
            manifestPlaceholders["appName"] = "RnDemo GPay"
            manifestPlaceholders["appIcon"] = "@mipmap/ic_launcher_gpay"
        }
    }

    signingConfigs {
        create("release") {
            // 读取签名配置文件
            val keystorePropertiesFile = rootProject.file("keystore.properties")
            if (keystorePropertiesFile.exists()) {
                val keystoreProperties = Properties()
                keystoreProperties.load(keystorePropertiesFile.inputStream())

                // 修复文件路径 - 密钥文件在android根目录下，不是上级目录
                val storeFileName = keystoreProperties["MYAPP_UPLOAD_STORE_FILE"]?.toString()
                val storePasswordValue = keystoreProperties["MYAPP_UPLOAD_STORE_PASSWORD"]?.toString()
                val keyAliasValue = keystoreProperties["MYAPP_UPLOAD_KEY_ALIAS"]?.toString()
                val keyPasswordValue = keystoreProperties["MYAPP_UPLOAD_KEY_PASSWORD"]?.toString()

                if (storeFileName != null && storePasswordValue != null && keyAliasValue != null && keyPasswordValue != null) {
                    storeFile = rootProject.file(storeFileName)
                    storePassword = storePasswordValue
                    keyAlias = keyAliasValue
                    keyPassword = keyPasswordValue

                    // 详细调试信息
                    println("=== 签名配置加载成功 ===")
                    println("Keystore file: ${storeFile}")
                    println("File exists: ${storeFile?.exists()}")
                    println("Key alias: ${keyAlias}")
                    println("Store password set: ${storePassword?.isNotEmpty()}")
                    println("Key password set: ${keyPassword?.isNotEmpty()}")
                    println("========================")
                } else {
                    println("ERROR: 签名配置不完整")
                    println("Store file: $storeFileName")
                    println("Store password set: ${storePasswordValue != null}")
                    println("Key alias: $keyAliasValue")
                    println("Key password set: ${keyPasswordValue != null}")
                }
            } else {
                println("WARNING: keystore.properties not found at ${keystorePropertiesFile.absolutePath}")
                // 备用配置（仅用于测试，不安全）
                storeFile = file("../my-release-key.keystore")
                storePassword = "password"
                keyAlias = "my-key-alias"
                keyPassword = "password"
            }
        }
    }

    buildTypes {
        debug {
            manifestPlaceholders["CLEAR_TEXT"] = true
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            manifestPlaceholders["CLEAR_TEXT"] = false
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // 确保release构建类型使用签名配置
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    viewBinding {
        enable = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("com.facebook.react:react-android")
    implementation("com.facebook.react:hermes-android")
}
react {
      // 启用自动链接需要添加以下行，参考： https://github.com/react-native-community/cli/blob/master/docs/autolinking.md
       autolinkLibrariesWithApp()
}
