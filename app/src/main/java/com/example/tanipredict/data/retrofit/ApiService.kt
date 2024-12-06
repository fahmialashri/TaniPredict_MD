package com.example.tanipredict.data.retrofit

import com.example.tanipredict.data.LoginResponse
import com.example.tanipredict.data.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("api/auth/register")
    suspend fun register(
        @Body requestBody: RegisterRequest
    ): RegisterResponse

    @POST("api/auth/login")
    suspend fun login(
        @Body requestBody: LoginRequest
    ): LoginResponse
}

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)

data class LoginRequest(
    val email: String,
    val password: String
)