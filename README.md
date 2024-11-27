# Proyecto Integrado MRA

## Descripción del proyecto

**Proyecto Integrado MRA** es una aplicación diseñada para conectar ofertantes y consumidores de actividades. Los ofertantes pueden organizar actividades de ocio, entretenimiento, deporte o aventura, mientras que los consumidores pueden encontrar y participar en dichas actividades o solicitar servicios específicos como guías o instructores. La plataforma centraliza la información para facilitar la interacción entre ambos grupos, permitiendo una experiencia fluida y accesible.

---

## Tecnologías utilizadas

- **Firebase**:
    - **Firestore**: Base de datos para gestionar usuarios y publicaciones.
    - **Firebase Authentication**: Manejo de autenticación de usuarios.
- **Kotlin**: Lenguaje principal para el desarrollo de la lógica de negocio y la interfaz.
- **Arquitectura MVVM**: Estructura base del proyecto para organizar el código de forma eficiente.
- **Jetpack Components**: Uso de LiveData, ViewModel y Coroutines para manejo de datos asíncronos y persistencia en la UI.

---

## Funcionalidades principales

### Usuarios

1. **Ofertantes**:
    
    - Registrarse, iniciar sesión, editar o eliminar su cuenta.
    - Publicar, modificar y eliminar actividades.
    - Buscar y visualizar anuncios de los consumidores.
2. **Consumidores**:
    
    - Registrarse, iniciar sesión, editar o eliminar su cuenta.
    - Publicar, modificar y eliminar anuncios solicitando actividades.
    - Buscar y visualizar actividades publicadas por los ofertantes.

### Funcionalidades comunes

- Interfaz gráfica atractiva y fácil de usar.
- Gestión centralizada de datos para sincronización en múltiples dispositivos.
- Validación de datos para evitar inconsistencias.
- Capacidad de inscripción en actividades (funcionalidad extra opcional).

---

## Estructura de la base de datos

### Colecciones principales:

1. **usuarios**:
    
    - **Campos**:
        - `name` (nombre del usuario).
        - `email` (correo electrónico).
        - `type` (tipo de usuario: ofertante o demandante).
2. **publicaciones**:
    
    - **Campos**:
        - `title` (título de la actividad).
        - `description` (descripción detallada).
        - `date` (fecha de la actividad).
        - `size` (número de plazas disponibles).
        - `ownerId` (ID del usuario creador de la publicación).
        - `participantes` (lista de IDs de usuarios inscritos).
        - `type`  (tipo de publicación: actividad o búsqueda).

---

## Arquitectura de la aplicación

### Diagrama de flujo (simplificado)

1. **Vista** (UI): Interactúa con el usuario, maneja las acciones y los eventos.
2. **ViewModel**: Contiene la lógica de negocio y proporciona datos listos para la UI.
3. **Modelo**: Representa los datos (usuarios, publicaciones) gestionados por Firestore.

---

## Cómo usar el proyecto

1. **Clonar el repositorio**:
    
    ``bash``
    
    **Copiar código**
    
    `git clone https://github.com/marromagu/Proyecto-Integrado-MRA.git cd proyecto-integrado-mra`
    
2. **Configurar Firebase**:
    
    - Crea un proyecto en Firebase.
    - Configura Firestore y Authentication.
    - Descarga el archivo `google-services.json` y colócalo en el directorio raíz del proyecto.
3. **Ejecutar la aplicación**:
    
    - Abre el proyecto en Android Studio.
    - Conecta un dispositivo o inicia un emulador.
    - Ejecuta el proyecto.
