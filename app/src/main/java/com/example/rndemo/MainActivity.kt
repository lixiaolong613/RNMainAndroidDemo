package com.example.rndemo

import android.content.Intent
import android.os.Bundle
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

        binding.tvContent.setOnClickListener {
            showRNFragment()
//            showRNActivity()
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