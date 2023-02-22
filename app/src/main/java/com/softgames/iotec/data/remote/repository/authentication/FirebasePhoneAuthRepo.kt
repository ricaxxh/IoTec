package com.softgames.iotec.data.remote.repository.authentication

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.softgames.iotec.domain.model.FirebaseAuthResponse
import com.softgames.iotec.presentation.authentication.login.phone_auth.model.FirebaseCodeResponse
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import java.util.concurrent.TimeUnit

class FirebasePhoneAuthRepo {

    companion object {

        val auth = Firebase.auth

        fun sendVerificationCode(
            phone_number: String,
            activity: Activity
        ) = callbackFlow {

            val callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    trySend(FirebaseCodeResponse.AUTOMAMTIC_VERIFICATION(credential))
                    close()
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    super.onCodeSent(verificationId, token)
                    trySend(FirebaseCodeResponse.ON_CODE_SEND(verificationId, token))
                    close()
                }

                override fun onVerificationFailed(exception: FirebaseException) {
                    trySend(FirebaseCodeResponse.FAILURE(exception))
                    close()
                }

            }

            val options = buildOptions(phone_number, activity, callback)
            auth.setLanguageCode("es-mx")
            PhoneAuthProvider.verifyPhoneNumber(options)
            trySend(FirebaseCodeResponse.LOADING)
            awaitClose()
        }

        private fun buildOptions(
            phone_number: String,
            activity: Activity,
            callback: PhoneAuthProvider.OnVerificationStateChangedCallbacks
        ): PhoneAuthOptions {
            return PhoneAuthOptions.newBuilder()
                .setPhoneNumber(phone_number)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(callback)
                .build()
        }

        fun generateCredential(verificationId: String, sms_code: String): PhoneAuthCredential {
            return PhoneAuthProvider.getCredential(verificationId, sms_code)
        }

        fun signInWithPhoneAuthCredential(
            activity: Activity,
            credential: PhoneAuthCredential,
        ) = callbackFlow {

            auth.signInWithCredential(credential)
                .addOnCompleteListener(activity) { task ->
                    trySend(FirebaseAuthResponse.COMPLETE(task))
                    close()
                }
                .addOnFailureListener { exception ->
                    trySend(FirebaseAuthResponse.FAILURE(exception))
                    close()
                }
            awaitClose()
        }

    }
}