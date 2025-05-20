package com.universidad.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import com.universidad.model.Materia;

import jakarta.persistence.LockModeType;

@Repository
public interface MateriaRepository extends JpaRepository<Materia, Long> {
    Materia findByCodigoUnico(String codigoUnico);

    @Lock(LockModeType.PESSIMISTIC_WRITE) // Bloqueo pesimista para evitar condiciones de carrera
    Optional<Materia> findById(Long id);
}
