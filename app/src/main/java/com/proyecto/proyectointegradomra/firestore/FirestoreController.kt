package com.proyecto.proyectointegradomra.firestore

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.proyecto.proyectointegradomra.Data.Usuario


class FirestoreController {

    private val db: FirebaseFirestore = Firebase.firestore

    private fun agregarDocumento(
        collectionPath: String,
        documentId: String?,
        data: Map<String, Any>,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection(collectionPath)
            .document(documentId ?: db.collection(collectionPath).document().id).set(data)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    fun eliminarDocumento(
        collectionPath: String,
        documentId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val documentRef =
            FirebaseFirestore.getInstance().collection(collectionPath).document(documentId)

        documentRef.delete()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }


    fun agregarDocumentoUsuario(usuario: Usuario) {
        val firestoreController = FirestoreController()
        val userProfile = mapOf(
            "nombre" to usuario.nombre, "email" to usuario.correo, "tipo" to usuario.tipo.name
        )

        firestoreController.agregarDocumento(collectionPath = "usuarios",
            documentId = usuario.uid,
            data = userProfile,
            onSuccess = {
                println("Perfil de usuario agregado correctamente.")
            },
            onFailure = { exception ->
                println("Error al agregar perfil de usuario: ${exception.message}")
            })
    }

    fun obtenerUsuarioPorUid(
        uid: String,
        onSuccess: (Usuario) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val documentRef = FirebaseFirestore.getInstance().collection("usuarios").document(uid)

        documentRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val usuario = document.toObject(Usuario::class.java)
                    if (usuario != null) {
                        onSuccess(usuario)
                    } else {
                        onFailure(Exception("No se pudo convertir el documento a Usuario"))
                    }
                } else {
                    onFailure(Exception("El documento no existe"))
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }


    fun actualizarNombreUsuario(
        uid: String,
        newName: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val userRef = db.collection("usuarios").document(uid)

        userRef.update("nombre", newName)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }


}
