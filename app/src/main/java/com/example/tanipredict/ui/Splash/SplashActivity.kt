package com.example.tanipredict.ui.Splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.tanipredict.ui.Login.MainActivity
import com.example.tanipredict.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Menunda selama 3 detik sebelum pindah ke Activity berikutnya (misalnya LoginActivity)
        Handler().postDelayed({
            // Pindah ke LoginActivity setelah splash screen
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()  // Menutup SplashActivity agar tidak kembali ke layar splash
        }, 3000) // 3000ms = 3 detik
    }
}
