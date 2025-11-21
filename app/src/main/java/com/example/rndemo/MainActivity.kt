package com.example.rndemo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.rndemo.databinding.ActivityMainBinding
import com.facebook.react.ReactFragment
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler

class MainActivity : AppCompatActivity(), DefaultHardwareBackBtnHandler {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 使用 SubModuleDemoAPI
        testSubModule()

        binding.tvContent.setOnClickListener {
            showRNFragment()
//            showRNActivity()
        }
    }

    private fun testSubModule() {
        try {
            // 初始化 SubModule
            com.common.SubModuleDemoAPI.initialize()

            // 获取问候语
            val greeting = com.common.SubModuleDemoAPI.getGreeting()
            Log.d("MainActivity", "Greeting from SubModule: $greeting")

            // 获取版本信息
            val version = com.common.SubModuleDemoAPI.getVersion()
            Log.d("MainActivity", "SubModule Version: $version")

            // 测试计算功能
            val result = com.common.SubModuleDemoAPI.add(5, 3)
            Log.d("MainActivity", "SubModule calculation 5 + 3 = $result")
        } catch (e: Exception) {
            Log.e("MainActivity", "Error using SubModule: ${e.message}")
        }
    }

    fun showRNFragment() {
        val launchOptions = Bundle().apply {
            putString("message", "Hello from Android1111!")
            putString("variant", "gpay")  // 可以是 "gpay" 或 "default"
            putInt("userId", 12345)
            putBoolean("isDebug", BuildConfig.DEBUG)
            putString("timestamp", System.currentTimeMillis().toString())
            putString("deviceInfo", "${android.os.Build.MODEL} - Android ${android.os.Build.VERSION.RELEASE}")
        }
        val fragment = ReactFragment.Builder()
            .setComponentName("HelloWorld")
            .setLaunchOptions(launchOptions)
            .build()
        supportFragmentManager.beginTransaction()
            .add(R.id.react_native_fragment, fragment)
            .commit()
    }

    fun showRNActivity() {
        val intent = Intent(this, MyReactActivity::class.java)
        startActivity(intent)
    }

    override fun invokeDefaultOnBackPressed() {
        super.onBackPressed()
    }
}