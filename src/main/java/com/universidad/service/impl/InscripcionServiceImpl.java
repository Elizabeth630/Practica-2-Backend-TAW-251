package com.universidad.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.universidad.dto.InscripcionDTO;
import com.universidad.model.Estudiante;
import com.universidad.model.Inscripcion;
import com.universidad.model.Materia;
import com.universidad.repository.EstudianteRepository;
import com.universidad.repository.InscripcionRepository;
import com.universidad.repository.MateriaRepository;
import com.universidad.service.IInscripcionService;

@Service
public class InscripcionServiceImpl implements IInscripcionService {

    private static final int MAX_MATERIAS_POR_ESTUDIANTE = 5;
    private static final int MAX_ESTUDIANTES_POR_MATERIA = 30;

    @Autowired
    private InscripcionRepository inscripcionRepository;
    
    @Autowired
    private EstudianteRepository estudianteRepository;
    
    @Autowired
    private MateriaRepository materiaRepository;

    @Override
    @Cacheable(value = "inscripciones")
    public List<InscripcionDTO> obtenerTodasLasInscripciones() {
        return inscripcionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "inscripcion", key = "#id")
    public InscripcionDTO obtenerInscripcionPorId(Long id) {
        return inscripcionRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    @Cacheable(value = "inscripcionesPorEstudiante", key = "#estudianteId")
    public List<InscripcionDTO> obtenerInscripcionesPorEstudiante(Long estudianteId) {
        return inscripcionRepository.findByEstudianteId(estudianteId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "inscripcionesPorMateria", key = "#materiaId")
    public List<InscripcionDTO> obtenerInscripcionesPorMateria(Long materiaId) {
        return inscripcionRepository.findByMateriaId(materiaId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @CachePut(value = "inscripcion", key = "#result.id")
    @CacheEvict(value = {"inscripciones", "inscripcionesPorEstudiante", "inscripcionesPorMateria"}, allEntries = true)
    public InscripcionDTO crearInscripcion(InscripcionDTO inscripcionDTO) {
        // Validaciones
        Estudiante estudiante = estudianteRepository.findById(inscripcionDTO.getEstudianteId())
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));
        
        Materia materia = materiaRepository.findById(inscripcionDTO.getMateriaId())
                .orElseThrow(() -> new RuntimeException("Materia no encontrada"));
        
        // Verificar si ya está inscrito en la materia
        if (inscripcionRepository.existsByEstudianteIdAndMateriaIdAndEstado(
                inscripcionDTO.getEstudianteId(), 
                inscripcionDTO.getMateriaId(), 
                "activo")) {
            throw new RuntimeException("El estudiante ya está inscrito en esta materia");
        }
        
        // Verificar cupos
        if (!verificarDisponibilidadMateria(inscripcionDTO.getMateriaId())) {
            throw new RuntimeException("No hay cupos disponibles en esta materia");
        }
        
        // Verificar límite de materias por estudiante
        if (!verificarCupoEstudiante(inscripcionDTO.getEstudianteId())) {
            throw new RuntimeException("El estudiante ha alcanzado el límite de materias permitidas");
        }
        
        // Verificar prerequisitos
        if (!verificarPrerequisitos(inscripcionDTO.getEstudianteId(), inscripcionDTO.getMateriaId())) {
            throw new RuntimeException("El estudiante no cumple con los prerequisitos para esta materia");
        }
        
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setEstudiante(estudiante);
        inscripcion.setMateria(materia);
        inscripcion.setFechaInscripcion(LocalDate.now());
        inscripcion.setEstado("activo");
        inscripcion.setPeriodoAcademico(inscripcionDTO.getPeriodoAcademico());
        inscripcion.setUsuarioRegistro(inscripcionDTO.getUsuarioRegistro());
        
        Inscripcion savedInscripcion = inscripcionRepository.save(inscripcion);
        return convertToDTO(savedInscripcion);
    }

    @Override
    @Transactional
    @CachePut(value = "inscripcion", key = "#id")
    @CacheEvict(value = {"inscripciones", "inscripcionesPorEstudiante", "inscripcionesPorMateria"}, allEntries = true)
    public InscripcionDTO actualizarInscripcion(Long id, InscripcionDTO inscripcionDTO) {
        return inscripcionRepository.findById(id)
                .map(existingInscripcion -> {
                    existingInscripcion.setEstado(inscripcionDTO.getEstado());
                    existingInscripcion.setCalificacion(inscripcionDTO.getCalificacion());
                    existingInscripcion.setPeriodoAcademico(inscripcionDTO.getPeriodoAcademico());
                    Inscripcion updatedInscripcion = inscripcionRepository.save(existingInscripcion);
                    return convertToDTO(updatedInscripcion);
                })
                .orElse(null);
    }

    @Override
    @CacheEvict(value = {"inscripcion", "inscripciones", "inscripcionesPorEstudiante", "inscripcionesPorMateria"}, allEntries = true)
    public void eliminarInscripcion(Long id) {
        inscripcionRepository.deleteById(id);
    }

    @Override
    @Transactional
    @CachePut(value = "inscripcion", key = "#id")
    @CacheEvict(value = {"inscripciones", "inscripcionesPorEstudiante", "inscripcionesPorMateria"}, allEntries = true)
    public InscripcionDTO actualizarEstadoInscripcion(Long id, String estado) {
        return inscripcionRepository.findById(id)
                .map(inscripcion -> {
                    inscripcion.setEstado(estado);
                    Inscripcion updatedInscripcion = inscripcionRepository.save(inscripcion);
                    return convertToDTO(updatedInscripcion);
                })
                .orElse(null);
    }

    @Override
    @Transactional
    @CachePut(value = "inscripcion", key = "#id")
    @CacheEvict(value = {"inscripciones", "inscripcionesPorEstudiante", "inscripcionesPorMateria"}, allEntries = true)
    public InscripcionDTO registrarCalificacion(Long id, Integer calificacion) {
        return inscripcionRepository.findById(id)
                .map(inscripcion -> {
                    inscripcion.setCalificacion(calificacion);
                    inscripcion.setEstado(calificacion >= 60 ? "aprobado" : "reprobado");
                    Inscripcion updatedInscripcion = inscripcionRepository.save(inscripcion);
                    return convertToDTO(updatedInscripcion);
                })
                .orElse(null);
    }

    @Override
    public boolean verificarDisponibilidadMateria(Long materiaId) {
        long inscripcionesActivas = inscripcionRepository.findActiveByMateriaId(materiaId).size();
        return inscripcionesActivas < MAX_ESTUDIANTES_POR_MATERIA;
    }

    @Override
    public boolean verificarCupoEstudiante(Long estudianteId) {
        long materiasActivas = inscripcionRepository.findActiveByEstudianteId(estudianteId).size();
        return materiasActivas < MAX_MATERIAS_POR_ESTUDIANTE;
    }

    @Override
    public boolean verificarPrerequisitos(Long estudianteId, Long materiaId) {
        Materia materia = materiaRepository.findById(materiaId)
                .orElseThrow(() -> new RuntimeException("Materia no encontrada"));
        
        if (materia.getPrerequisitos() == null || materia.getPrerequisitos().isEmpty()) {
            return true;
        }
        
        List<Long> materiasAprobadas = inscripcionRepository.findByEstudianteId(estudianteId).stream()
                .filter(i -> "aprobado".equals(i.getEstado()))
                .map(i -> i.getMateria().getId())
                .collect(Collectors.toList());
        
        return materiasAprobadas.containsAll(
            materia.getPrerequisitos().stream()
                .map(Materia::getId)
                .collect(Collectors.toList())
        );
    }

    private InscripcionDTO convertToDTO(Inscripcion inscripcion) {
        return InscripcionDTO.builder()
                .id(inscripcion.getId())
                .estudianteId(inscripcion.getEstudiante().getId())
                .materiaId(inscripcion.getMateria().getId())
                .fechaInscripcion(inscripcion.getFechaInscripcion())
                .estado(inscripcion.getEstado())
                .calificacion(inscripcion.getCalificacion())
                .periodoAcademico(inscripcion.getPeriodoAcademico())
                .usuarioRegistro(inscripcion.getUsuarioRegistro())
                .build();
    }
}