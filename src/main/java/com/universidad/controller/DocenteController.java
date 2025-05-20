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
import org.springframework.web.bind.annotation.RestController;

import com.universidad.dto.DocenteDTO;
import com.universidad.service.IDocenteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/docentes")
@SecurityRequirement(name = "bearerAuth")
public class DocenteController {

    @Autowired
    private IDocenteService docenteService;

    @GetMapping
    @Operation(summary = "Obtener todos los docentes", description = "Requiere rol ADMIN o DOCENTE")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCENTE')")
    public ResponseEntity<List<DocenteDTO>> obtenerTodosLosDocentes() {
        List<DocenteDTO> docentes = docenteService.obtenerTodosLosDocentes();
        return ResponseEntity.ok(docentes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener docente por ID", description = "Requiere rol ADMIN o DOCENTE")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCENTE')")
    public ResponseEntity<DocenteDTO> obtenerDocentePorId(@PathVariable Long id) {
        DocenteDTO docente = docenteService.obtenerDocentePorId(id);
        if (docente == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(docente);
    }

    @GetMapping("/empleado/{nroEmpleado}")
    @Operation(summary = "Obtener docente por número de empleado", description = "Requiere rol ADMIN o DOCENTE")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCENTE')")
    public ResponseEntity<DocenteDTO> obtenerDocentePorNroEmpleado(@PathVariable String nroEmpleado) {
        DocenteDTO docente = docenteService.obtenerDocentePorNroEmpleado(nroEmpleado);
        if (docente == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(docente);
    }

    @PostMapping
    @Operation(summary = "Crear nuevo docente", description = "Requiere rol ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DocenteDTO> crearDocente(@RequestBody DocenteDTO docenteDTO) {
        DocenteDTO nuevoDocente = docenteService.crearDocente(docenteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoDocente);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar docente", description = "Requiere rol ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DocenteDTO> actualizarDocente(@PathVariable Long id, @RequestBody DocenteDTO docenteDTO) {
        DocenteDTO docenteActualizado = docenteService.actualizarDocente(id, docenteDTO);
        if (docenteActualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(docenteActualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar docente", description = "Requiere rol ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarDocente(@PathVariable Long id) {
        docenteService.eliminarDocente(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{docenteId}/asignar-materias")
    @Operation(summary = "Asignar materias a docente", description = "Requiere rol ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DocenteDTO> asignarMateriasADocente(
            @PathVariable Long docenteId, 
            @RequestBody List<Long> materiasIds) {
        DocenteDTO docenteActualizado = docenteService.asignarMateriasADocente(docenteId, materiasIds);
        return ResponseEntity.ok(docenteActualizado);
    }

    @GetMapping("/por-materia/{materiaId}")
    @Operation(summary = "Obtener docentes por materia", description = "Requiere rol ADMIN o DOCENTE")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCENTE')")
    public ResponseEntity<List<DocenteDTO>> obtenerDocentesPorMateria(@PathVariable Long materiaId) {
        List<DocenteDTO> docentes = docenteService.obtenerDocentesPorMateria(materiaId);
        return ResponseEntity.ok(docentes);
    }
}