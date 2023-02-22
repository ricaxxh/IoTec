package com.softgames.iotec.data.remote.repository.authentication

object FirebaseGoogleAuthRepo {
    suspend fun signInWithGoogle(idToken: String): Boolean {
        return true
    }
}