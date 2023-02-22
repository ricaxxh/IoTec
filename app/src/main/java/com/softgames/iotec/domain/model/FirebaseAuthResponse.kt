package com.softgames.iotec.domain.model

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import java.lang.Exception
import java.util.*

sealed class FirebaseAuthResponse {

    object LOADING : FirebaseAuthResponse()

    data class COMPLETE(
        val task: Task<AuthResult>
    ) : FirebaseAuthResponse()

    data class FAILURE(
        val exception: Exception
    ) : FirebaseAuthResponse()
}
