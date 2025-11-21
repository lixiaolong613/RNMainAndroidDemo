package com.example.rndemo

import com.facebook.react.ReactActivity
import com.facebook.react.ReactActivityDelegate
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint
import com.facebook.react.defaults.DefaultReactActivityDelegate
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise
import android.content.Context
import android.os.Bundle

class MyReactActivity: ReactActivity() {
    companion object{
        const val NAME_TAG = "HelloWorld"
    }

    override fun getMainComponentName(): String? {
        return NAME_TAG
    }

    override fun createReactActivityDelegate(): ReactActivityDelegate {
        return object: DefaultReactActivityDelegate(this, mainComponentName?:NAME_TAG, DefaultNewArchitectureEntryPoint.fabricEnabled) {
            override fun getLaunchOptions(): Bundle? {
                //这里传递数据给RN
                return super.getLaunchOptions()
            }
        }
    }
}

//// 添加原生模块来获取变体信息
//class AppVariantModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
//
//    override fun getName(): String {
//        return "AppVariant"
//    }
//
//    @ReactMethod
//    fun getAppVariant(promise: Promise) {
//        try {
//            val context = reactApplicationContext
//            val packageName = context.packageName
//
//            // 根据包名判断变体
//            val variant = when {
//                packageName.contains(".gpay") -> "gpay"
//                else -> "default"
//            }
//
//            promise.resolve(variant)
//        } catch (e: Exception) {
//            promise.reject("VARIANT_ERROR", e.message, e)
//        }
//    }
//}