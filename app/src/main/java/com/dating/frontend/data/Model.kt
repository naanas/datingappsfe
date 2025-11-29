package com.dating.frontend.data

data class LoginRequest(val email: String, val password: String)

data class LoginResponse(
    val token: String,
    val user: User
)

data class User(
    val id: String, // UUID dari backend
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
    val action: String // "LIKE" atau "PASS"
)