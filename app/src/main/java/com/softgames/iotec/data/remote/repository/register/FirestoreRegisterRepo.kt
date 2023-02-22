package com.softgames.iotec.data.remote.repository.register

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.softgames.iotec.domain.model.USER_TYPE
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class FirestoreRegisterRepo {

    companion object {

        fun registerAdminAndOrganization(
            user_id: String,
            register_data: HashMap<String, String?>
        ) = callbackFlow {

            Firebase.firestore.collection("Usuarios")
                .document("Tipos")
                .collection(USER_TYPE.ADMIN.ROUTE)
                .document(user_id).set(register_data)
                .addOnSuccessListener {

                    Firebase.firestore.collection("Organizaciones")
                        .document(register_data["Código"]!!)
                        .set(
                            hashMapOf(
                                "Administrador" to register_data["Nombre"]!!,
                                "Nombre" to register_data["Organización"]!!,
                                "Dispositivos" to ""
                            )
                        ).addOnSuccessListener {
                            trySend(Unit)
                            close()
                        }
                        .addOnFailureListener {
                            Log.e("IoTec", it.message!!)
                            close()
                        }

                }
                .addOnFailureListener {
                    Log.i("IoTec", it.message!!)
                    close()
                }
            awaitClose()
        }

        fun registerTeacher(
            user_id: String,
            register_data: HashMap<String, String?>
        ) = callbackFlow {
            Firebase.firestore.collection("Usuarios")
                .document("Tipos")
                .collection(USER_TYPE.TEACHER.ROUTE)
                .document(user_id).set(register_data)
                .addOnSuccessListener {
                    trySend(Unit)
                    close()
                }
            awaitClose()
        }

        fun searchOrganization(organization_id: String) = callbackFlow {
            Firebase.firestore.collection("Organizaciones")
                .document(organization_id)
                .get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        val name = it.data?.get("Nombre") as String
                        trySend(name)
                        close()
                    } else {
                        trySend(null)
                        close()
                    }
                }
            awaitClose()
        }

    }
}