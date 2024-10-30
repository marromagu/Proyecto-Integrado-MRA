package com.proyecto.proyectointegradomra.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.proyecto.proyectointegradomra.authentication.AuthController


class FirestoreController {

    private val db: FirebaseFirestore = Firebase.firestore

    fun agregarDocumento(
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

    fun leerDocumento(
        collectionPath: String,
        documentId: String,
        onSuccess: (Map<String, Any>?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection(collectionPath).document(documentId).get().addOnSuccessListener { document ->
                if (document.exists()) {
                    onSuccess(document.data)
                } else {
                    onSuccess(null)
                }
            }.addOnFailureListener { exception -> onFailure(exception) }
    }

    fun actualizarDocumento(
        collectionPath: String,
        documentId: String,
        data: Map<String, Any>,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection(collectionPath).document(documentId).update(data)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    fun eliminarDocumento(
        collectionPath: String,
        documentId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection(collectionPath).document(documentId).delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    fun agregarDocumnetoUsuario(uid: String?, nombre: String, email: String, tipo: AuthController.TipoUsuario) {
        val firestoreController = FirestoreController()
        val userProfile = mapOf(
            "nombre" to nombre, "email" to email, "tipo" to tipo.name.lowercase()
        )

        firestoreController.agregarDocumento(collectionPath = "usuarios",
            documentId = uid,
            data = userProfile,
            onSuccess = {
                println("Perfil de usuario agregado correctamente.")
            },
            onFailure = { exception ->
                println("Error al agregar perfil de usuario: ${exception.message}")
            })
    }
    fun obtenerNombreUsuarioPorUid(
        uid: String,
        onSuccess: (String?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("usuarios").document(uid).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    onSuccess(document.data?.get("nombre") as? String)
                } else {
                    onSuccess(null) // Usuario no encontrado
                }
            }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

}
