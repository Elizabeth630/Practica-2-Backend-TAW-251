package com.universidad.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.universidad.dto.EstudianteDTO;
import com.universidad.model.Estudiante;
import com.universidad.model.Materia;
import com.universidad.service.IEstudianteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/estudiantes")
@Validated
@SecurityRequirement(name = "bearerAuth")
public class EstudianteController {

    private final IEstudianteService estudianteService;
    private static final Logger logger = LoggerFactory.getLogger(EstudianteController.class);

    @Autowired
    public EstudianteController(IEstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los estudiantes", description = "Requiere rol ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EstudianteDTO>> obtenerTodosLosEstudiantes() {
        long inicio = System.currentTimeMillis();
        logger.info("[ESTUDIANTE] Inicio obtenerTodosLosEstudiantes: {}", inicio);
        List<EstudianteDTO> estudiantes = estudianteService.obtenerTodosLosEstudiantes();
        long fin = System.currentTimeMillis();
        logger.info("[ESTUDIANTE] Fin obtenerTodosLosEstudiantes: {} (Duracion: {} ms)", fin, (fin-inicio));
        return ResponseEntity.ok(estudiantes);
    }

    @GetMapping("/inscripcion/{numeroInscripcion}")
    @Operation(summary = "Obtener estudiante por número de inscripción", description = "Requiere rol ADMIN o DOCENTE")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCENTE')")
    public ResponseEntity<EstudianteDTO> obtenerEstudiantePorNumeroInscripcion(
        @PathVariable String numeroInscripcion) {
        long inicio = System.currentTimeMillis();
        logger.info("[ESTUDIANTE] Inicio obtenerEstudiantePorNumeroInscripcion: {}", inicio);
        EstudianteDTO estudiante = estudianteService.obtenerEstudiantePorNumeroInscripcion(numeroInscripcion);
        long fin = System.currentTimeMillis();
        logger.info("[ESTUDIANTE] Fin obtenerEstudiantePorNumeroInscripcion: {} (Duracion: {} ms)", fin, (fin-inicio));
        return ResponseEntity.ok(estudiante);
    }

    @GetMapping("/{id}/materias")
    @Operation(summary = "Obtener materias de un estudiante", description = "Requiere rol ADMIN, DOCENTE o ESTUDIANTE (solo propias)")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCENTE') or (hasRole('ESTUDIANTE') and #estudianteId == authentication.principal.id)")
    public ResponseEntity<List<Materia>> obtenerMateriasDeEstudiante(
        @PathVariable("id") Long estudianteId) {
        List<Materia> materias = estudianteService.obtenerMateriasDeEstudiante(estudianteId);
        return ResponseEntity.ok(materias);
    }

    @GetMapping("/{id}/lock")
    @Operation(summary = "Obtener estudiante con bloqueo", description = "Requiere rol ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Estudiante> getEstudianteConBloqueo(
        @PathVariable Long id) {
        Estudiante estudiante = estudianteService.obtenerEstudianteConBloqueo(id);
        return ResponseEntity.ok(estudiante);
    }

    @PostMapping
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear nuevo estudiante", description = "Requiere rol ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EstudianteDTO> crearEstudiante(@Valid @RequestBody EstudianteDTO estudianteDTO) {
        EstudianteDTO nuevoEstudiante = estudianteService.crearEstudiante(estudianteDTO);
        return ResponseEntity.status(201).body(nuevoEstudiante);
    }

    @PutMapping("/{id}")
    @Transactional
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Actualizar estudiante", description = "Requiere rol ADMIN o ESTUDIANTE (solo propio)")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('ESTUDIANTE') and #id == authentication.principal.id)")
    public ResponseEntity<EstudianteDTO> actualizarEstudiante(
        @PathVariable Long id,
        @RequestBody EstudianteDTO estudianteDTO) {
        EstudianteDTO estudianteActualizado = estudianteService.actualizarEstudiante(id, estudianteDTO);
        return ResponseEntity.ok(estudianteActualizado);
    }

    @PutMapping("/{id}/baja")
    @Transactional
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Dar de baja a un estudiante", description = "Requiere rol ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EstudianteDTO> eliminarEstudiante(
        @PathVariable Long id,
        @RequestBody EstudianteDTO estudianteDTO) {
        EstudianteDTO estudianteEliminado = estudianteService.eliminarEstudiante(id, estudianteDTO);
        return ResponseEntity.ok(estudianteEliminado);
    }

    @GetMapping("/activos")
    @Operation(summary = "Obtener estudiantes activos", description = "Requiere rol ADMIN o DOCENTE")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCENTE')")
    public ResponseEntity<List<EstudianteDTO>> obtenerEstudianteActivo() {
        List<EstudianteDTO> estudiantesActivos = estudianteService.obtenerEstudianteActivo();
        return ResponseEntity.ok(estudiantesActivos);
    }
}