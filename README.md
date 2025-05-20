# 🎓 Proyecto Universidad - Sistema de Gestión Académica con Spring Boot

Este proyecto es un sistema backend para gestionar docentes, estudiantes, inscripciones, materias y evaluaciones desarrollado con Spring Boot.  
Incluye seguridad basada en roles, CRUD completo, validaciones y autenticación JWT.

---

## 🚀 Tecnologías utilizadas  
- Java 17  
- Spring Boot  
- Maven  
- Spring Security con JWT  
- PostgreSQL (o base de datos relacional)  
- Postman para pruebas de API REST  

---

## 📂 Estructura del proyecto

```plaintext
src/
└── main/
    ├── java/
    │   └── com.universidad/
    │       ├── controller/
    │       │   ├── DocenteController.java
    │       │   ├── EstudianteController.java
    │       │   ├── EvaluacionDocenteController.java
    │       │   ├── InscripcionController.java
    │       │   └── MateriaController.java
    │       ├── dto/
    │       │   ├── DocenteDTO.java
    │       │   ├── EstudianteDTO.java
    │       │   └── MateriaDTO.java
    │       ├── model/
    │       │   ├── Docente.java
    │       │   ├── Estudiante.java
    │       │   ├── EvaluacionDocente.java
    │       │   ├── Materia.java
    │       │   └── Persona.java
    │       ├── registro/
    │       │   ├── registro/config/
    │       │   │   ├── DatabaseInitializer.java
    │       │   │   └── SecurityConfig.java
    │       │   ├── registro/controller/
    │       │   │   ├── AuthController.java
    │       │   │   └── UsuarioController.java
    │       │   ├── registro/dto/
    │       │   │   └── AuthDTO.java
    │       │   ├── registro/model/
    │       │   │   ├── Rol.java
    │       │   │   └── Usuario.java
    │       │   ├── registro/repository/
    │       │   │   ├── RolRepository.java
    │       │   │   └── UsuarioRepositoryl.java
    │       │   ├── registro/security/
    │       │   │   ├── JwtAuthenticationEntryPoint.java
    │       │   │   ├── JwtAuthenticationFilter.java
    │       │   │   └── JwtUtils.java
    │       │   ├── registro/service/
    │       │   │   └── UserDetailsServiceImpl.java
    │       │   ├── repository/
    │       │   │   ├── DocenteRepository.java
    │       │   │   ├── EstudianteRepository.java
    │       │   │   ├── EvaluacionDocenteRepository.java
    │       │   │   ├── InscripcionRepository.java
    │       │   │   └── MateriaRepository.java
    │       │   ├── service/
    │       │   │   ├── IDocenteService.java
    │       │   │   ├── IEstudianteService.java
    │       │   │   ├── IEvaluacionDocenteService.java
    │       │   │   ├── IInscripcionService.java
    │       │   │   └── IMateriaService.java
    │       │   ├── service/impl/
    │       │   │   ├── DocenteServiceImpl.java
    │       │   │   ├── EstudianteServiceImpl.java
    │       │   │   ├── EvaluacionDocenteServiceImpl.java
    │       │   │   ├── InscripcionServiceImpl.java
    │       │   │   └── MateriaServiceImpl.java
    │       │   ├── validation/
    │       │   │   ├── ApiError.java
    │       │   │   ├── EstudianteValidator.java
    │       │   │   └── GlobalExceptionHandler.java
    │       │   └── AppApplication.java
    ├── resources/
    │   └── application.properties


## 📌 Endpoints principales  
(La aplicación expone múltiples endpoints REST, algunos ejemplos:)

| Método  | URL                               | Descripción                               |
|---------|----------------------------------|-------------------------------------------|
| GET     | `/api/estudiantes`               | Obtener todos los estudiantes             |
| POST    | `/api/estudiantes`               | Crear un estudiante                       |
| PUT     | `/api/estudiantes/{id}`          | Actualizar un estudiante                  |
| DELETE  | `/api/estudiantes/{id}`          | Eliminar un estudiante                    |
| GET     | `/api/inscripciones`             | Obtener todas las inscripciones           |
| POST    | `/api/inscripciones`             | Crear nueva inscripción                   |
| PUT     | `/api/inscripciones/{id}/estado`| Actualizar estado de inscripción          |

---

## 📖 Cómo ejecutar el proyecto

1. Clona el repositorio  
```bash
git clone https://github.com/Elizabeth630/Practica-2-Backend-TAW-251.git

