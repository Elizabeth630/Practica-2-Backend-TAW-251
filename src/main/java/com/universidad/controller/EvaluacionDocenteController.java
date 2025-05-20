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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.universidad.model.EvaluacionDocente;
import com.universidad.service.IEvaluacionDocenteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/evaluaciones-docente")
@SecurityRequirement(name = "bearerAuth")
public class EvaluacionDocenteController {
    @Autowired
    private IEvaluacionDocenteService evaluacionDocenteService;

    @PostMapping
    @Operation(summary = "Crear evaluación docente", description = "Requiere rol ESTUDIANTE")
    @PreAuthorize("hasRole('ESTUDIANTE')")
    public ResponseEntity<EvaluacionDocente> crearEvaluacion(@RequestBody EvaluacionDocente evaluacion) {
        EvaluacionDocente nueva = evaluacionDocenteService.crearEvaluacion(evaluacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    @GetMapping("/docente/{docenteId}")
    @Operation(summary = "Obtener evaluaciones por docente", description = "Requiere rol ADMIN o DOCENTE (solo propias)")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('DOCENTE') and #docenteId == authentication.principal.id)")
    public ResponseEntity<List<EvaluacionDocente>> obtenerEvaluacionesPorDocente(@PathVariable Long docenteId) {
        List<EvaluacionDocente> evaluaciones = evaluacionDocenteService.obtenerEvaluacionesPorDocente(docenteId);
        return ResponseEntity.ok(evaluaciones);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener evaluación por ID", description = "Requiere rol ADMIN o DOCENTE (solo propias)")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('DOCENTE') and @evaluacionDocenteService.obtenerEvaluacionPorId(#id).docente.id == authentication.principal.id)")
    public ResponseEntity<EvaluacionDocente> obtenerEvaluacionPorId(@PathVariable Long id) {
        EvaluacionDocente evaluacion = evaluacionDocenteService.obtenerEvaluacionPorId(id);
        if (evaluacion == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(evaluacion);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar evaluación", description = "Requiere rol ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarEvaluacion(@PathVariable Long id) {
        evaluacionDocenteService.eliminarEvaluacion(id);
        return ResponseEntity.noContent().build();
    }
}