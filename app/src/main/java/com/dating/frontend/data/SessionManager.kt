package com.dating.frontend.data

object SessionManager {
    var jwtToken: String? = null
    var currentUser: User? = null

    fun getAuthHeader(): String {
        return "Bearer $jwtToken"
    }
}