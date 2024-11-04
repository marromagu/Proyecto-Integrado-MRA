package com.proyecto.proyectointegradomra.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.proyecto.proyectointegradomra.Data.TipoUsuario
import com.proyecto.proyectointegradomra.Data.Usuario
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
        uid: String, onSuccess: (Usuario) -> Unit, onFailure: (Exception) -> Unit
    ) {
        db.collection("usuarios").document(uid).get().addOnSuccessListener { document ->
            if (document.exists()) {
                val usuario = document.toObject(Usuario::class.java)
                if (usuario != null) {
                    onSuccess(usuario)
                } else {
                    onFailure(Exception("Error al convertir documento a Usuario"))
                }
            } else {
                onFailure(Exception("Usuario no encontrado"))
            }
        }.addOnFailureListener { exception -> onFailure(exception) }
    }

    fun actualizarNombreUsuario(
        uid: String,
        newName: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        // Acceder al documento del usuario en Firestore
        val userRef = db.collection("usuarios").document(uid)

        // Actualizar el nombre del usuario
        userRef.update("nombre", newName)
            .addOnSuccessListener {
                onSuccess() // Llamar a onSuccess si la actualización fue exitosa
            }
            .addOnFailureListener { exception ->
                onFailure(exception) // Llamar a onFailure si hubo un error
            }
    }


}
