package com.universidad.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.universidad.model.Docente;

public interface DocenteRepository extends JpaRepository<Docente, Long> {
    // Método para buscar docente por número de empleado
    Optional<Docente> findByNroEmpleado(String nroEmpleado);

    // Método para buscar docentes que enseñan una materia específica
    List<Docente> findByMaterias_Id(Long materiaId);
}