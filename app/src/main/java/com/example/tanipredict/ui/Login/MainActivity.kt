package com.example.tanipredict.ui.Login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tanipredict.R
import com.example.tanipredict.data.SharedPreferencesManager
import com.example.tanipredict.data.retrofit.ApiConfig
import com.example.tanipredict.data.retrofit.LoginRequest
import com.example.tanipredict.ui.Dashboard.DashboardActivity
import com.example.tanipredict.ui.register.RegisterActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        usernameEditText = findViewById(R.id.editTextUsername)
        passwordEditText = findViewById(R.id.editTextPassword)
        loginButton = findViewById(R.id.submitButton)
        registerTextView = findViewById(R.id.signUpText) // Inisialisasi

        // Buat tautan untuk RegisterActivity
        registerTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            val email = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                val loginRequest = LoginRequest(email, password)

                // Cek apakah token ada. Jika ada, lakukan login dengan token tersebut
                val token = SharedPreferencesManager.getToken(applicationContext)

                // Jika token ada, lakukan login dengan token tersebut
                if (token != null) {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val apiService = ApiConfig.getApiServiceWithToken(token)
                            val response = apiService.login(loginRequest)

                            Log.d("Login Token", "Using saved token to login")

                            if (response.success) {
                                runOnUiThread {
                                    // Log untuk memastikan login sukses
                                    Log.d("Login Success", "Login successful with token")

                                    // Redirect ke DashboardActivity
                                    val intent = Intent(this@MainActivity, DashboardActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            } else {
                                runOnUiThread {
                                    Toast.makeText(
                                        applicationContext,
                                        "Login failed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } catch (e: Exception) {
                            runOnUiThread {
                                Toast.makeText(
                                    applicationContext,
                                    "Network error",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                } else {
                    // Jika token tidak ada, lakukan login dengan email dan password
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val apiService = ApiConfig.getApiService()
                            val response = apiService.login(loginRequest)

                            Log.d("Login Attempt", "Attempting to login with email and password")

                            if (response.success) {
                                // Setelah login berhasil, simpan token yang diterima dari API
                                SharedPreferencesManager.saveToken(
                                    applicationContext,
                                    response.token
                                )

                                // Log token yang disimpan
                                Log.d("Saved Token", "Token saved successfully: ${response.token}")

                                runOnUiThread {
                                    // Log untuk memastikan login sukses
                                    Log.d("Login Success", "Login successful, redirecting to Dashboard")

                                    // Redirect ke DashboardActivity setelah login sukses
                                    val intent = Intent(this@MainActivity, DashboardActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            } else {
                                runOnUiThread {
                                    Toast.makeText(
                                        applicationContext,
                                        "Login failed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } catch (e: Exception) {
                            runOnUiThread {
                                Toast.makeText(
                                    applicationContext,
                                    "Network error",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            } else {
                // Tampilkan error jika username atau password kosong
                Toast.makeText(applicationContext, "Please fill in both fields", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}
