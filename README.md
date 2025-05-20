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

## 📂 Estructura del proyecto principal (`src/main/java/com/universidad`)

src/
└── main/
├── java/
│ └── com.universidad/
│ ├── controller/ # Controladores REST para manejar solicitudes HTTP
│ │ ├── DocenteController.java
│ │ ├── EstudianteController.java
│ │ ├── EvaluacionDocenteController.java
│ │ ├── InscripcionController.java
│ │ └── MateriaController.java
│ ├── dto/ # Clases DTO para transferencia de datos entre capas
│ │ ├── DocenteDTO.java
│ │ ├── EstudianteDTO.java
│ │ └── MateriaDTO.java
│ ├── model/ # Entidades JPA que representan las tablas de la base de datos
│ │ ├── Docente.java
│ │ ├── Estudiante.java
│ │ ├── EvaluacionDocente.java
│ │ ├── Materia.java
│ │ └── Persona.java
│ ├── registro/ # Módulo para manejo de usuarios, roles y seguridad
│ │ ├── config/ # Configuración de seguridad y base de datos
│ │ │ ├── DatabaseInitializer.java
│ │ │ └── SecurityConfig.java
│ │ ├── controller/ # Controladores para autenticación y manejo de usuarios
│ │ │ ├── AuthController.java
│ │ │ └── UsuarioController.java
│ │ ├── dto/ # DTO para autenticación
│ │ │ └── AuthDTO.java
│ │ ├── model/ # Modelos de seguridad: usuario y rol
│ │ │ ├── Rol.java
│ │ │ └── Usuario.java
│ │ ├── repository/ # Repositorios para acceso a datos de seguridad
│ │ │ ├── RolRepository.java
│ │ │ └── UsuarioRepositoryl.java
│ │ ├── security/ # Clases para JWT y filtros de seguridad
│ │ │ ├── JwtAuthenticationEntryPoint.java
│ │ │ ├── JwtAuthenticationFilter.java
│ │ │ └── JwtUtils.java
│ │ ├── service/ # Servicios relacionados a usuarios
│ │ │ └── UserDetailsServiceImpl.java
│ ├── repository/ # Repositorios para entidades principales (Docente, Estudiante, etc.)
│ │ ├── DocenteRepository.java
│ │ ├── EstudianteRepository.java
│ │ ├── EvaluacionDocenteRepository.java
│ │ ├── InscripcionRepository.java
│ │ └── MateriaRepository.java
│ ├── service/ # Interfaces de servicios para la lógica de negocio
│ │ ├── IDocenteService.java
│ │ ├── IEstudianteService.java
│ │ ├── IEvaluacionDocenteService.java
│ │ ├── IInscripcionService.java
│ │ └── IMateriaService.java
│ ├── service/impl/ # Implementaciones concretas de los servicios
│ │ ├── DocenteServiceImpl.java
│ │ ├── EstudianteServiceImpl.java
│ │ ├── EvaluacionDocenteServiceImpl.java
│ │ ├── InscripcionServiceImpl.java
│ │ └── MateriaServiceImpl.java
│ ├── validation/ # Validadores personalizados y manejo global de excepciones
│ │ ├── ApiError.java
│ │ ├── EstudianteValidator.java
│ │ └── GlobalExceptionHandler.java
│ └── AppApplication.java # Clase principal para levantar la aplicación Spring Boot
├── resources/
│ └── application.properties # Configuraciones de la aplicación (base de datos, puerto, JWT, etc.)


---

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

