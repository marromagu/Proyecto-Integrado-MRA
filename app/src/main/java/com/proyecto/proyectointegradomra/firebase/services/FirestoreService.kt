package com.proyecto.proyectointegradomra.firebase.services

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.proyecto.proyectointegradomra.data.model.Publicaciones
import com.proyecto.proyectointegradomra.data.model.TipoPublicaciones
import com.proyecto.proyectointegradomra.data.model.Usuario
import kotlinx.coroutines.tasks.await

/**
 * Servicio para manejar las operaciones relacionadas con Firestore.
 * Incluye métodos para gestionar usuarios y publicaciones en la base de datos.
 */
class FirestoreService {

    // Instancia singleton para interactuar con Firestore.
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    /**
     * Agrega un documento a la colección especificada.
     * @param collectionPath Ruta de la colección.
     * @param documentId ID del documento (opcional).
     * @param data Datos que se agregarán al documento.
     */
    private fun agregarDocumentoFirestore(
        collectionPath: String, documentId: String?, data: Map<String, Any>
    ) {
        val documentRef = firestore.collection(collectionPath).document(
            documentId ?: firestore.collection(collectionPath).document().id
        )
        documentRef.set(data).addOnSuccessListener {
            Log.i("FirestoreService", "Documento agregado con éxito: ${documentRef.id}")
        }.addOnFailureListener { exception ->
            Log.e("FirestoreService", "Error al agregar documento: $exception")
        }
    }

    /**
     * Elimina un documento de la colección especificada.
     * @param collectionPath Ruta de la colección.
     * @param documentId ID del documento que se eliminará.
     */
    fun eliminarDocumentoFirestore(collectionPath: String, documentId: String) {
        if (documentId.isBlank()) {
            Log.e("FirestoreService", "El ID del documento es inválido para la eliminación.")
            return
        }

        firestore.collection(collectionPath).document(documentId).delete().addOnSuccessListener {
            Log.i("FirestoreService", "Documento eliminado con éxito: $documentId")
        }.addOnFailureListener { exception ->
            Log.e("FirestoreService", "Error al eliminar documento: $exception")
        }
    }

    /**
     * Agrega un nuevo usuario a Firestore.
     * @param usuario Objeto `Usuario` con los datos del usuario a agregar.
     */
    fun agregarDocumentoUsuarioFirestore(usuario: Usuario) {
        val usuarioData = mapOf(
            "name" to usuario.name, "email" to usuario.email, "type" to usuario.type.name
        )
        agregarDocumentoFirestore("usuarios", usuario.uid, usuarioData)
    }

    /**
     * Obtiene un usuario por su UID.
     * @param uid UID del usuario.
     * @return Objeto `Usuario` o `null` si no se encuentra.
     */
    suspend fun obtenerUsuarioPorUidFirestore(uid: String): Usuario? {
        return try {
            val documentSnapshot = firestore.collection("usuarios").document(uid).get().await()
            val usuario = documentSnapshot.toObject<Usuario>()
            usuario?.copy(uid = documentSnapshot.id) // Crear una nueva instancia con el UID actualizado
        } catch (exception: Exception) {
            Log.e("FirestoreService", "Error al obtener usuario por UID: $exception")
            null
        }
    }


    /**
     * Actualiza el nombre de un usuario en Firestore.
     * @param uid UID del usuario.
     * @param nuevoNombre Nuevo nombre del usuario.
     */
    fun actualizarNombreUsuarioFirestore(uid: String, nuevoNombre: String) {
        if (uid.isBlank()) {
            Log.e("FirestoreService", "El UID es inválido para la actualización del nombre.")
            return
        }

        firestore.collection("usuarios").document(uid).update("name", nuevoNombre)
            .addOnSuccessListener {
                Log.i("FirestoreService", "Nombre del usuario actualizado con éxito.")
            }.addOnFailureListener { exception ->
                Log.e("FirestoreService", "Error al actualizar nombre: $exception")
            }
    }

    /**
     * Agrega una nueva publicación a Firestore.
     * @param publicacion Objeto `Publicaciones` con los datos de la publicación.
     */
    fun agregarDocumentoPublicacionesFirestore(publicacion: Publicaciones) {
        val publicacionData = mapOf(
            "userId" to publicacion.userId,
            "title" to publicacion.title,
            "description" to publicacion.description,
            "date" to publicacion.date,
            "plazas" to publicacion.plazas,
            "tipo" to publicacion.tipo,
            "participantes" to publicacion.participantes
        )
        agregarDocumentoFirestore("publicaciones", null, publicacionData)
    }

    /**
     * Obtiene publicaciones por usuario.
     * @param userId ID del usuario.
     * @return Lista de publicaciones del usuario.
     */
    suspend fun obtenerPublicacionesPorUsuario(userId: String): List<Publicaciones> {
        return try {
            firestore.collection("publicaciones").whereEqualTo("userId", userId).get()
                .await().documents.mapNotNull {
                    it.toObject<Publicaciones>()?.apply { uid = it.id }
                }
        } catch (exception: Exception) {
            Log.e("FirestoreService", "Error al obtener publicaciones por usuario: $exception")
            emptyList()
        }
    }

    /**
     * Obtiene una lista de publicaciones de un tipo específico en las que el usuario no haya participado.
     * @param tipo El tipo de publicación (por ejemplo, actividad o anuncio de búsqueda).
     * @param uid El identificador único del usuario.
     * @return Una lista de objetos `Publicaciones` en las que el usuario no haya participado.
     */
    suspend fun obtenerPublicacionesPorTipoSinParticipar(
        tipo: TipoPublicaciones, uid: String
    ): List<Publicaciones> {
        return try {
            val querySnapshot = firestore.collection("publicaciones")
                .whereEqualTo("tipo", tipo)
                .get()
                .await()

            querySnapshot.documents.mapNotNull { document ->
                val publicacion = document.toObject<Publicaciones>()
                if (!publicacion?.participantes?.contains(uid)!!) { // Lo tengo q filtrar aqui por el indice de mierda no lo hace bien
                    publicacion.uid = document.id
                    publicacion
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            Log.e("FirestoreService", "Error al obtener publicaciones: $e")
            emptyList()
        }
    }

    /**
     * Obtiene una lista de publicaciones de un tipo específico en las que el usuario ya haya participado.
     * @param tipo El tipo de publicación (por ejemplo, actividad o anuncio de búsqueda).
     * @param uid El identificador único del usuario.
     * @return Una lista de objetos `Publicaciones` en las que el usuario haya participado.
     */
    suspend fun obtenerPublicacionesParticipadas(
        tipo: TipoPublicaciones, uid: String
    ): List<Publicaciones> {
        return try {
            val querySnapshot =
                firestore.collection("publicaciones").whereEqualTo("tipo", tipo).whereEqualTo(
                    "participantes", listOf(uid)
                ).get().await()
            querySnapshot.documents.mapNotNull { document ->
                val publicaciones = document.toObject<Publicaciones>()
                publicaciones?.uid = document.id
                publicaciones
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Agrega un participante a una publicación.
     * @param uid UID del participante.
     * @param publicacionId ID de la publicación.
     */
    fun agregarParticipante(uid: String, publicacionId: String) {
        firestore.collection("publicaciones").document(publicacionId)
            .update("participantes", FieldValue.arrayUnion(uid)).addOnSuccessListener {
                Log.i("FirestoreService", "Participante agregado con éxito.")
            }.addOnFailureListener { exception ->
                Log.e("FirestoreService", "Error al agregar participante: $exception")
            }
    }

    /**
     * Elimina un participante de una publicación.
     * @param uid UID del participante.
     * @param publicacionId ID de la publicación.
     */
    fun eliminarParticipante(uid: String, publicacionId: String) {
        firestore.collection("publicaciones").document(publicacionId)
            .update("participantes", FieldValue.arrayRemove(uid)).addOnSuccessListener {
                Log.i("FirestoreService", "Participante eliminado con éxito.")
            }.addOnFailureListener { exception ->
                Log.e("FirestoreService", "Error al eliminar participante: $exception")
            }
    }
    
    /**
     * Elimina una publicación.
     * @param publicacionId ID de la publicación.
     */
    fun eliminarPublicacion(publicacionId: String) {
        eliminarDocumentoFirestore("publicaciones", publicacionId)
    }
}
