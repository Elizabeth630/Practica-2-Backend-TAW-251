package com.universidad.service;

import java.util.List;

import com.universidad.dto.DocenteDTO;

public interface IDocenteService {
    List<DocenteDTO> obtenerTodosLosDocentes();
    DocenteDTO obtenerDocentePorId(Long id);
    DocenteDTO obtenerDocentePorNroEmpleado(String nroEmpleado);
    DocenteDTO crearDocente(DocenteDTO docenteDTO);
    DocenteDTO actualizarDocente(Long id, DocenteDTO docenteDTO);
    void eliminarDocente(Long id);
    DocenteDTO asignarMateriasADocente(Long docenteId, List<Long> materiasIds);
    List<DocenteDTO> obtenerDocentesPorMateria(Long materiaId);
}