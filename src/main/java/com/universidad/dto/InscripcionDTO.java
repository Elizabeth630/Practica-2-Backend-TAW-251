package com.universidad.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InscripcionDTO {
    private Long id;
    private Long estudianteId;
    private Long materiaId;
    private LocalDate fechaInscripcion;
    private String estado; // "activo", "aprobado", "reprobado", "retirado"
    private Integer calificacion;
    private String periodoAcademico;
    private String usuarioRegistro;
}