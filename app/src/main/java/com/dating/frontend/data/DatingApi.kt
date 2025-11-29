package com.dating.frontend.data

import retrofit2.http.*

interface DatingApi {
    @POST("login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("feed")
    suspend fun getFeed(
        @Header("Authorization") token: String,
        @Query("userId") userId: String
    ): List<User>

    @POST("swipe")
    suspend fun swipe(
        @Header("Authorization") token: String,
        @Body request: SwipeRequest
    ): Map<String, Any>
}