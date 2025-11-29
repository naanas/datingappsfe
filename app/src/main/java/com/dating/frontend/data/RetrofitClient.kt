package com.dating.frontend.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8081/api/"

    // Buat interceptor buat ngintip log
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Ini kuncinya! Bisa liat isi JSON
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val api: DatingApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client) // Pasang client yang ada logger-nya
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DatingApi::class.java)
    }
}