package com.example.rndemo

import android.app.Application
import com.facebook.react.PackageList
import com.facebook.react.ReactApplication
import com.facebook.react.ReactHost
import com.facebook.react.ReactNativeHost
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint
import com.facebook.react.defaults.DefaultReactHost
import com.facebook.react.defaults.DefaultReactNativeHost
import com.facebook.react.soloader.OpenSourceMergedSoMapping
import com.facebook.soloader.SoLoader
import com.webview.ReactWebViewPackage
import java.util.*

class MyApplication: Application(), ReactApplication {

    override val reactNativeHost: ReactNativeHost
        get() = object : DefaultReactNativeHost(this) {
            override fun getPackages(): List<ReactPackage> =
                PackageList(this).packages + listOf(
//                    AppVariantPackage(),
                    NativeLocalStoragePackage(),
                    ReactWebViewPackage()
                )

            override fun getJSMainModuleName(): String = "index"

            override fun getUseDeveloperSupport(): Boolean = BuildConfig.DEBUG

            override val isHermesEnabled: Boolean?
                get() = BuildConfig.IS_HERMES_ENABLED

            override val isNewArchEnabled: Boolean
                get() = BuildConfig.IS_NEW_ARCHITECTURE_ENABLED
        }

    override val reactHost: ReactHost?
        get() = DefaultReactHost.getDefaultReactHost(applicationContext, reactNativeHost)


    override fun onCreate() {
        super.onCreate()
        SoLoader.init(this, OpenSourceMergedSoMapping)
        if (BuildConfig.IS_NEW_ARCHITECTURE_ENABLED) {
            DefaultNewArchitectureEntryPoint.load()
        }
    }
}

// 添加 Package 类来注册原生模块
//class AppVariantPackage : ReactPackage {
//    override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
//        return listOf(AppVariantModule(reactContext))
//    }
//
//
//    override fun createViewManagers(reactContext: ReactApplicationContext): List<com.facebook.react.uimanager.ViewManager<*, *>> {
//        return emptyList()
//    }
//}