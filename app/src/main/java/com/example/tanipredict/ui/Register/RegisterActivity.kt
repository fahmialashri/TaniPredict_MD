package com.example.tanipredict.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tanipredict.R
import com.example.tanipredict.data.retrofit.ApiConfig
import com.example.tanipredict.data.retrofit.RegisterRequest
import com.example.tanipredict.data.RegisterResponse
import com.example.tanipredict.ui.Login.MainActivity
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterActivity : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextReenterPassword: EditText
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize views
        editTextName = findViewById(R.id.editTextName)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        editTextReenterPassword = findViewById(R.id.editTextReenterPassword)
        btnRegister = findViewById(R.id.buttonRegister)

        // Set onClickListener for register button
        btnRegister.setOnClickListener {
            val username = editTextName.text.toString().trim()
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()
            val reenterPassword = editTextReenterPassword.text.toString().trim()

            // Validation
            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || reenterPassword.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Check if passwords match
            if (password != reenterPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Register user (API call)
            register(username, email, password)
        }
    }

    private fun register(username: String, email: String, password: String) {
        lifecycleScope.launch {
            try {
                // Call API for registration (without token)
                val apiService = ApiConfig.getApiService() // No token needed here
                val request = RegisterRequest(username, email, password)

                // Make the request and get response
                val response: RegisterResponse = apiService.register(request)

                // Handle the response
                if (response.success) {
                    // Show success message
                    Toast.makeText(
                        this@RegisterActivity,
                        "Registration successful",
                        Toast.LENGTH_LONG
                    ).show()

                    // Store token (if available) using SharedPreferences
                    val token = response.token // Assuming token is part of RegisterResponse
                    saveTokenToPreferences(token)

                    // Navigate to the login page
                    val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                    intent.putExtra("successMessage", "Registration successful! Please log in.")
                    startActivity(intent)
                    finish() // Close RegisterActivity
                } else {
                    // Handle failure (e.g., show error message)
                    Toast.makeText(
                        this@RegisterActivity,
                        "Registration failed: ${response.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } catch (e: HttpException) {
                // Handle HTTP errors
                Toast.makeText(this@RegisterActivity, "HTTP Error: ${e.message}", Toast.LENGTH_LONG)
                    .show()
            } catch (e: Throwable) {
                // Handle other errors
                Toast.makeText(
                    this@RegisterActivity,
                    "Something went wrong: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    // Save token to SharedPreferences
    private fun saveTokenToPreferences(token: String) {
        val sharedPreferences = getSharedPreferences("TaniPredictPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("auth_token", token) // Save the token
        editor.apply()
    }
}
