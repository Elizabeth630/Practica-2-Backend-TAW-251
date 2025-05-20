package com.universidad.service.impl;

import com.universidad.dto.DocenteDTO;
import com.universidad.model.Docente;
import com.universidad.model.Materia;
import com.universidad.repository.DocenteRepository;
import com.universidad.repository.MateriaRepository;
import com.universidad.service.IDocenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocenteServiceImpl implements IDocenteService {

    @Autowired
    private DocenteRepository docenteRepository;
    
    @Autowired
    private MateriaRepository materiaRepository;

    @Override
    @Cacheable(value = "docentes")
    public List<DocenteDTO> obtenerTodosLosDocentes() {
        return docenteRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "docente", key = "#id")
    public DocenteDTO obtenerDocentePorId(Long id) {
        return docenteRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    @Cacheable(value = "docente", key = "#nroEmpleado")
    public DocenteDTO obtenerDocentePorNroEmpleado(String nroEmpleado) {
        return docenteRepository.findByNroEmpleado(nroEmpleado)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    @CachePut(value = "docente", key = "#result.id")
    @CacheEvict(value = "docentes", allEntries = true)
    public DocenteDTO crearDocente(DocenteDTO docenteDTO) {
        Docente docente = convertToEntity(docenteDTO);
        Docente savedDocente = docenteRepository.save(docente);
        return convertToDTO(savedDocente);
    }

    @Override
    @CachePut(value = "docente", key = "#id")
    @CacheEvict(value = "docentes", allEntries = true)
    public DocenteDTO actualizarDocente(Long id, DocenteDTO docenteDTO) {
        return docenteRepository.findById(id)
                .map(existingDocente -> {
                    updateEntityFromDTO(existingDocente, docenteDTO);
                    Docente updatedDocente = docenteRepository.save(existingDocente);
                    return convertToDTO(updatedDocente);
                })
                .orElse(null);
    }

    @Override
    @CacheEvict(value = {"docente", "docentes"}, allEntries = true)
    public void eliminarDocente(Long id) {
        docenteRepository.deleteById(id);
    }

    @Override
    @Transactional
    @CachePut(value = "docente", key = "#docenteId")
    @CacheEvict(value = {"docentes", "materias"}, allEntries = true)
    public DocenteDTO asignarMateriasADocente(Long docenteId, List<Long> materiasIds) {
        Docente docente = docenteRepository.findById(docenteId)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado"));
        
        List<Materia> materias = materiaRepository.findAllById(materiasIds);
        docente.setMaterias(materias);
        
        Docente updatedDocente = docenteRepository.save(docente);
        return convertToDTO(updatedDocente);
    }

    @Override
    @Cacheable(value = "docentesPorMateria", key = "#materiaId")
    public List<DocenteDTO> obtenerDocentesPorMateria(Long materiaId) {
        return docenteRepository.findByMaterias_Id(materiaId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private DocenteDTO convertToDTO(Docente docente) {
        return DocenteDTO.builder()
                .id(docente.getId())
                .nombre(docente.getNombre())
                .apellido(docente.getApellido())
                .email(docente.getEmail())
                .fechaNacimiento(docente.getFechaNacimiento())
                .nroEmpleado(docente.getNroEmpleado())
                .departamento(docente.getDepartamento())
                .materiasAsignadas(docente.getMaterias() != null ? 
                    docente.getMaterias().stream().map(Materia::getId).collect(Collectors.toList()) : null)
                .build();
    }

    private Docente convertToEntity(DocenteDTO docenteDTO) {
        return Docente.builder()
                .id(docenteDTO.getId())
                .nombre(docenteDTO.getNombre())
                .apellido(docenteDTO.getApellido())
                .email(docenteDTO.getEmail())
                .fechaNacimiento(docenteDTO.getFechaNacimiento())
                .nroEmpleado(docenteDTO.getNroEmpleado())
                .departamento(docenteDTO.getDepartamento())
                .build();
    }

    private void updateEntityFromDTO(Docente docente, DocenteDTO docenteDTO) {
        docente.setNombre(docenteDTO.getNombre());
        docente.setApellido(docenteDTO.getApellido());
        docente.setEmail(docenteDTO.getEmail());
        docente.setFechaNacimiento(docenteDTO.getFechaNacimiento());
        docente.setNroEmpleado(docenteDTO.getNroEmpleado());
        docente.setDepartamento(docenteDTO.getDepartamento());
    }
}