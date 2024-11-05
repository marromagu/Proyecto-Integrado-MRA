package com.proyecto.proyectointegradomra.firebase.database

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.proyecto.proyectointegradomra.data.Usuario


class FirestoreController {

    private val miCloudFirestore: FirebaseFirestore = Firebase.firestore

    private fun agregarDocumentoFirestore(collectionPath: String,documentId: String?,data: Map<String, Any>,onSuccess: () -> Unit,onFailure: (Exception) -> Unit) {
        miCloudFirestore
            .collection(collectionPath)
            .document(documentId ?: miCloudFirestore.collection(collectionPath).document().id)
            .set(data)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    fun eliminarDocumentoFirestore(collectionPath: String,documentId: String,onSuccess: () -> Unit,onFailure: (Exception) -> Unit) {
        val documentRef =FirebaseFirestore.getInstance().collection(collectionPath).document(documentId)

        documentRef.delete()
            .addOnSuccessListener {onSuccess()}
            .addOnFailureListener {exception ->onFailure(exception)}
    }

    fun agregarDocumentoUsuarioFirestore(usuario: Usuario) {
        val firestoreController = FirestoreController()
        val userProfile = mapOf("nombre" to usuario.nombre, "email" to usuario.email, "tipo" to usuario.tipo.name)

        firestoreController.agregarDocumentoFirestore(
            collectionPath = "usuarios",
            documentId = usuario.uid,
            data = userProfile,
            onSuccess = {println("Perfil de usuario agregado correctamente.")},
            onFailure = { exception ->println("Error al agregar perfil de usuario: ${exception.message}")})
    }

    fun obtenerUsuarioPorUidFirestore(uid: String,onSuccess: (Usuario) -> Unit,onFailure: (Exception) -> Unit) {
        val documentRef = FirebaseFirestore.getInstance().collection("usuarios").document(uid)

        documentRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    Log.d("FirestoreController", "Documento encontrado: ${document.data}")
                    val usuario = document.toObject(Usuario::class.java)
                        if (usuario != null) {
                            Log.d("FirestoreController", "Usuario obtenido: $usuario")
                            onSuccess(usuario)
                        } else {
                            onFailure(Exception("No se pudo convertir el documento a Usuario"))
                        }
                } else {
                    onFailure(Exception("El documento no existe"))
                }
            }
            .addOnFailureListener { exception ->onFailure(exception)}
    }

    fun actualizarNombreUsuarioFirestore(uid: String,newName: String,onSuccess: () -> Unit,onFailure: (Exception) -> Unit) {
        val userRef = miCloudFirestore.collection("usuarios").document(uid)

        userRef.update("nombre", newName)
            .addOnSuccessListener {onSuccess()}
            .addOnFailureListener { exception ->onFailure(exception)}
    }
}
