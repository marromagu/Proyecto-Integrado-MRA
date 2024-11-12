package com.proyecto.proyectointegradomra.firebase.services

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.proyecto.proyectointegradomra.data.model.Publicaciones
import com.proyecto.proyectointegradomra.data.model.Usuario
import kotlinx.coroutines.tasks.await

/**
 * Controlador para manejar las operaciones relacionadas con Firestore.
 * Proporciona métodos para agregar, eliminar y obtener documentos de la colección de usuarios.
 */
class FirestoreService {

    // Instancia singleton de FirebaseFirestore para interactuar con Firestore.
    private val miCloudFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    /**
     * Agrega un documento a la colección especificada.
     * Si no se proporciona un ID de documento, se genera uno automáticamente.
     *
     * @param collectionPath Ruta de la colección donde se agregará el documento.
     * @param documentId ID del documento (operador de llamada segura).
     * @param data Datos a almacenar en el documento.
     */
    private fun agregarDocumentoFirestore(
        collectionPath: String,
        documentId: String?,
        data: Map<String, Any>
    ) {
        // Obtiene referencia a la colección y documento, luego almacena los datos.
        miCloudFirestore
            .collection(collectionPath)
            .document(documentId ?: miCloudFirestore.collection(collectionPath).document().id)
            .set(data)
            .addOnSuccessListener {
                Log.i("FirestoreService", "Documento agregado con ID: $documentId")
            }
            .addOnFailureListener { exception ->
                // Manejo de error en caso de fallo al agregar el documento.
                Log.e("FirestoreService", "Error al agregar documento: $exception")
            }
    }

    /**
     * Elimina un documento de la colección especificada.
     *
     * @param collectionPath Ruta de la colección de la que se eliminará el documento.
     * @param documentId ID del documento que se va a eliminar.
     */
    fun eliminarDocumentoFirestore(collectionPath: String, documentId: String) {
        // Verifica que el documentId no sea nulo o vacío antes de proceder con la eliminación.
        if (documentId.isBlank()) {
            Log.e("FirestoreService", "ID de documento inválido para eliminar.")
            return
        }

        val documentRef = miCloudFirestore.collection(collectionPath).document(documentId)

        documentRef.delete()
            .addOnSuccessListener {
                Log.i("FirestoreService", "Documento eliminado con ID: $documentId")
            }
            .addOnFailureListener { exception ->
                // Manejo de error en caso de fallo al eliminar el documento.
                Log.e("FirestoreService", "Error al eliminar documento: $exception")
            }
    }

    /**
     * Agrega un nuevo usuario a la colección de usuarios en Firestore.
     *
     * @param miUsuario Objeto Usuario que contiene los datos a almacenar.
     */
    fun agregarDocumentoUsuarioFirestore(miUsuario: Usuario) {
        val userMapOf = mapOf(
            "name" to miUsuario.name,
            "email" to miUsuario.email,
            "type" to miUsuario.type.name
        )
        this.agregarDocumentoFirestore(
            collectionPath = "usuarios",
            documentId = miUsuario.uid,
            data = userMapOf
        )
    }

    /**
     * Obtiene un usuario por su UID desde Firestore de forma asíncrona.
     *
     * @param uid UID del usuario a buscar.
     * @return Usuario? Devuelve el usuario si se encuentra, o null si no existe o ocurre un error.
     */
    suspend fun obtenerUsuarioPorUidFirestore(uid: String): Usuario? {
        return try {
            val documentRef = miCloudFirestore.collection("usuarios").document(uid)
            val document = documentRef.get().await()

            // Verifica si el documento existe y lo convierte a Usuario.
            if (document.exists()) {
                document.toObject(Usuario::class.java)
            } else {
                Log.i("FirestoreService", "No se encontró el documento con UID: $uid")
                null
            }
        } catch (exception: Exception) {
            // Manejo de excepciones, loggear el error para la depuración.
            Log.e("FirestoreService", "Error al obtener usuario por UID: $exception")
            null
        }
    }

    /**
     * Actualiza el nombre de un usuario en Firestore.
     *
     * @param uid UID del usuario cuya información se va a actualizar.
     * @param newName Nuevo nombre que se almacenará.
     */
    fun actualizarNombreUsuarioFirestore(uid: String, newName: String) {
        if (uid.isBlank()) {
            Log.e("FirestoreService", "UID inválido para la actualización del nombre.")
            return
        }

        val userRef = miCloudFirestore.collection("usuarios").document(uid)

        userRef.update("name", newName)
            .addOnSuccessListener {
                Log.i("FirestoreService", "Nombre de usuario actualizado con éxito.")
            }
            .addOnFailureListener { exception ->
                // Manejo de error en caso de fallo al actualizar el nombre.
                Log.e("FirestoreService", "Error al actualizar nombre de usuario: $exception")
            }
    }

    /**
     * Agrega un documento de publicaciones a Firestore.
     *
     * @param miPublicacion Objeto Publicaciones que contiene los datos a almacenar.
     */
    fun agregarDocumentoPublicacionesFirestore(miPublicacion: Publicaciones) {
        val publicacionMapOf = mapOf(
            "userId" to miPublicacion.userId,
            "title" to miPublicacion.title,
            "description" to miPublicacion.description,
            "date" to miPublicacion.date,
            "plazas" to miPublicacion.plazas
        )
        this.agregarDocumentoFirestore(
            collectionPath = "publicaciones",
            documentId = null,
            data = publicacionMapOf
        )
    }

    suspend fun obtenerPublicacionesPorUsuario(userId: String): List<Publicaciones> {
        return try {
            val querySnapshot = miCloudFirestore.collection("publicaciones")
                .whereEqualTo("userId", userId)
                .get()
                .await()
            querySnapshot.documents.mapNotNull { it.toObject<Publicaciones>() }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun obtenerPublicaciones(userId: String): List<Publicaciones> {
        return try {

            val querySnapshot = miCloudFirestore.collection("publicaciones")
                .whereEqualTo("userId", userId)
                .get()
                .await()
            querySnapshot.documents.mapNotNull { it.toObject<Publicaciones>() }
        } catch (e: Exception) {
            emptyList()
        }
    }
}