package com.universidad.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.universidad.dto.InscripcionDTO;
import com.universidad.service.IInscripcionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/inscripciones")
@SecurityRequirement(name = "bearerAuth")
public class InscripcionController {

    @Autowired
    private IInscripcionService inscripcionService;

    @GetMapping
    @Operation(summary = "Obtener todas las inscripciones", description = "Requiere rol ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<InscripcionDTO>> obtenerTodasLasInscripciones() {
        List<InscripcionDTO> inscripciones = inscripcionService.obtenerTodasLasInscripciones();
        return ResponseEntity.ok(inscripciones);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener inscripción por ID", description = "Requiere rol ADMIN o DOCENTE")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCENTE')")
    public ResponseEntity<InscripcionDTO> obtenerInscripcionPorId(@PathVariable Long id) {
        InscripcionDTO inscripcion = inscripcionService.obtenerInscripcionPorId(id);
        if (inscripcion == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(inscripcion);
    }

    @GetMapping("/estudiante/{estudianteId}")
    @Operation(summary = "Obtener inscripciones por estudiante", description = "Requiere rol ADMIN, DOCENTE o ESTUDIANTE (solo propias)")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCENTE') or (hasRole('ESTUDIANTE') and #estudianteId == authentication.principal.id)")
    public ResponseEntity<List<InscripcionDTO>> obtenerInscripcionesPorEstudiante(@PathVariable Long estudianteId) {
        List<InscripcionDTO> inscripciones = inscripcionService.obtenerInscripcionesPorEstudiante(estudianteId);
        return ResponseEntity.ok(inscripciones);
    }

    @GetMapping("/materia/{materiaId}")
    @Operation(summary = "Obtener inscripciones por materia", description = "Requiere rol ADMIN o DOCENTE")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCENTE')")
    public ResponseEntity<List<InscripcionDTO>> obtenerInscripcionesPorMateria(@PathVariable Long materiaId) {
        List<InscripcionDTO> inscripciones = inscripcionService.obtenerInscripcionesPorMateria(materiaId);
        return ResponseEntity.ok(inscripciones);
    }

    @PostMapping
    @Operation(summary = "Crear nueva inscripción", description = "Requiere rol ADMIN o ESTUDIANTE (solo propias)")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('ESTUDIANTE') and #inscripcionDTO.estudianteId == authentication.principal.id)")
    public ResponseEntity<InscripcionDTO> crearInscripcion(@RequestBody InscripcionDTO inscripcionDTO) {
        InscripcionDTO nuevaInscripcion = inscripcionService.crearInscripcion(inscripcionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaInscripcion);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar inscripción", description = "Requiere rol ADMIN o DOCENTE")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCENTE')")
    public ResponseEntity<InscripcionDTO> actualizarInscripcion(
            @PathVariable Long id, 
            @RequestBody InscripcionDTO inscripcionDTO) {
        InscripcionDTO inscripcionActualizada = inscripcionService.actualizarInscripcion(id, inscripcionDTO);
        if (inscripcionActualizada == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(inscripcionActualizada);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar inscripción", description = "Requiere rol ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarInscripcion(@PathVariable Long id) {
        inscripcionService.eliminarInscripcion(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/estado")
    @Operation(summary = "Actualizar estado de inscripción", description = "Requiere rol ADMIN o DOCENTE")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCENTE')")
    public ResponseEntity<InscripcionDTO> actualizarEstadoInscripcion(
            @PathVariable Long id, 
            @RequestParam String estado) {
        InscripcionDTO inscripcionActualizada = inscripcionService.actualizarEstadoInscripcion(id, estado);
        if (inscripcionActualizada == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(inscripcionActualizada);
    }

    @PutMapping("/{id}/calificacion")
    @Operation(summary = "Registrar calificación", description = "Requiere rol DOCENTE")
    @PreAuthorize("hasRole('DOCENTE')")
    public ResponseEntity<InscripcionDTO> registrarCalificacion(
            @PathVariable Long id, 
            @RequestParam Integer calificacion) {
        InscripcionDTO inscripcionActualizada = inscripcionService.registrarCalificacion(id, calificacion);
        if (inscripcionActualizada == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(inscripcionActualizada);
    }
}