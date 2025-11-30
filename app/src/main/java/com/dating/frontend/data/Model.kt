package com.dating.frontend.data

data class LoginRequest(val email: String, val password: String)

data class LoginResponse(
    val token: String,
    val user: User
)

// Data class baru untuk Register
data class RegisterRequest(
    val email: String,
    val password: String,
    val fullName: String,
    val gender: String, // "MALE" atau "FEMALE"
    val bio: String
)

data class User(
    val id: String,
    val email: String,
    val fullName: String?,
    val gender: String?,
    val bio: String?,
    val photoUrl: String?,
    val jobTitle: String?,
    val company: String?
)

data class SwipeRequest(
    val myId: String,
    val targetId: String,
    val action: String
)