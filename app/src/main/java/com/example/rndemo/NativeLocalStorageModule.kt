package com.example.rndemo

import android.content.Context
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.myapp.generated.NativeLocalStorageSpec


class NativeLocalStorageModule(reactContext: ReactApplicationContext?) :
    NativeLocalStorageSpec(reactContext) {
    override fun getName(): String {
        return NAME
    }

    override fun setItem(value: String?, key: String?, promise: Promise?) {
        try {
            val sharedPref =
                reactApplicationContext.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString(key, value)
            editor.apply()
            
            // 成功时 resolve Promise
            promise?.resolve(null)
        } catch (e: Exception) {
            // 失败时 reject Promise
            promise?.reject("SET_ERROR", "Failed to set item: ${e.message}", e)
        }
    }

    override fun getItem(key: String?, promise: Promise?) {
        try {
            val sharedPref =
                reactApplicationContext.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
            val value = sharedPref.getString(key, null)
            
            // 根据我们之前的讨论，用空字符串表示不存在
            // 或者可以用 reject 来表示不存在
            if (value != null) {
                promise?.resolve(value)
            } else {
                // 选项1：返回空字符串表示不存在
                promise?.resolve("")
                
                // 选项2：reject 表示不存在（注释掉）
                // promise?.reject("KEY_NOT_FOUND", "Key '$key' not found")
            }
        } catch (e: Exception) {
            promise?.reject("GET_ERROR", "Failed to get item: ${e.message}", e)
        }
    }

    override fun removeItem(key: String?, promise: Promise?) {
        try {
            val sharedPref =
                reactApplicationContext.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
            sharedPref.edit().remove(key).apply()
            
            // 成功时 resolve Promise
            promise?.resolve(null)
        } catch (e: Exception) {
            // 失败时 reject Promise
            promise?.reject("REMOVE_ERROR", "Failed to remove item: ${e.message}", e)
        }
    }

    override fun clear(promise: Promise?) {
        try {
            val sharedPref =
                reactApplicationContext.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
            sharedPref.edit().clear().apply()
            
            // 成功时 resolve Promise
            promise?.resolve(null)
        } catch (e: Exception) {
            // 失败时 reject Promise
            promise?.reject("CLEAR_ERROR", "Failed to clear storage: ${e.message}", e)
        }
    }


    companion object {
        const val NAME: String = "NativeLocalStorage"
    }
}