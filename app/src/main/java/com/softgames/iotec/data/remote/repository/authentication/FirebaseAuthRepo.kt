package com.softgames.iotec.data.remote.repository.authentication

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class FirebaseAuthRepo {

    companion object {

        fun checkUserExist(user_id: String, user_route: String) = callbackFlow {

            Firebase.firestore
                .collection("Usuarios")
                .document("null")
                .collection(user_route)
                .document(user_id).get()
                .addOnSuccessListener { user ->
                    if (user.exists()) {
                        trySend(true)
                    } else {
                        trySend(false)
                    }
                    close()
                }

                .addOnFailureListener {
                    Log.i("ALDAIR", it.message!!)
                }
            awaitClose()
        }
    }
}