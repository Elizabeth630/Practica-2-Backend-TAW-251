package com.universidad.service;

import java.util.List;

import com.universidad.dto.InscripcionDTO;

public interface IInscripcionService {
    List<InscripcionDTO> obtenerTodasLasInscripciones();
    InscripcionDTO obtenerInscripcionPorId(Long id);
    List<InscripcionDTO> obtenerInscripcionesPorEstudiante(Long estudianteId);
    List<InscripcionDTO> obtenerInscripcionesPorMateria(Long materiaId);
    InscripcionDTO crearInscripcion(InscripcionDTO inscripcionDTO);
    InscripcionDTO actualizarInscripcion(Long id, InscripcionDTO inscripcionDTO);
    void eliminarInscripcion(Long id);
    InscripcionDTO actualizarEstadoInscripcion(Long id, String estado);
    InscripcionDTO registrarCalificacion(Long id, Integer calificacion);
    boolean verificarDisponibilidadMateria(Long materiaId);
    boolean verificarCupoEstudiante(Long estudianteId);
    boolean verificarPrerequisitos(Long estudianteId, Long materiaId);
}