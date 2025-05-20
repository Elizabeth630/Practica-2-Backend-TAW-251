package com.universidad.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.universidad.model.Inscripcion;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {
    List<Inscripcion> findByEstudianteId(Long estudianteId);
    List<Inscripcion> findByMateriaId(Long materiaId);
    boolean existsByEstudianteIdAndMateriaIdAndEstado(Long estudianteId, Long materiaId, String estado);
    
    @Query("SELECT i FROM Inscripcion i WHERE i.estudiante.id = :estudianteId AND i.estado = 'activo'")
    List<Inscripcion> findActiveByEstudianteId(@Param("estudianteId") Long estudianteId);
    
    @Query("SELECT i FROM Inscripcion i WHERE i.materia.id = :materiaId AND i.estado = 'activo'")
    List<Inscripcion> findActiveByMateriaId(@Param("materiaId") Long materiaId);
}