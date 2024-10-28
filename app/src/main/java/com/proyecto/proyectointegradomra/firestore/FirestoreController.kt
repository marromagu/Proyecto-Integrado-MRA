package com.proyecto.proyectointegradomra.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class FirestoreController {

    private val db: FirebaseFirestore = Firebase.firestore

    // Función para agregar un documento en una colección
    fun agregarDocumento(
        collectionPath: String,
        data: Map<String, Any>,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection(collectionPath)
            .add(data)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    // Función para leer un documento específico
    fun leerDocumento(
        collectionPath: String,
        documentId: String,
        onSuccess: (Map<String, Any>?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection(collectionPath).document(documentId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    onSuccess(document.data)
                } else {
                    onSuccess(null) // Documento no encontrado
                }
            }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    // Función para actualizar un documento
    fun actualizarDocumento(
        collectionPath: String,
        documentId: String,
        data: Map<String, Any>,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection(collectionPath).document(documentId)
            .update(data)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    // Función para eliminar un documento
    fun eliminarDocumento(
        collectionPath: String,
        documentId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection(collectionPath).document(documentId)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    fun agregarPerfilUsuario(nombre: String, email: String) {
        val firestoreController = FirestoreController()
        val userProfile = mapOf(
            "nombre" to nombre,
            "email" to email
        )

        firestoreController.agregarDocumento(
            collectionPath = "usuarios",
            data = userProfile,
            onSuccess = {
                println("Perfil de usuario agregado correctamente.")
            },
            onFailure = { exception ->
                println("Error al agregar perfil de usuario: ${exception.message}")
            }
        )
    }

}
