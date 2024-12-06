package com.example.tanipredict.data

import com.google.gson.annotations.SerializedName
data class LoginResponse(
    @SerializedName("loginResult")
    val loginResult: LoginResult,
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("token")
    val token: String
)

data class LoginResult(
    @SerializedName("name")
    val name: String,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("token")
    val token: String
)

data class RegisterResponse(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("token")
    val token: String
)