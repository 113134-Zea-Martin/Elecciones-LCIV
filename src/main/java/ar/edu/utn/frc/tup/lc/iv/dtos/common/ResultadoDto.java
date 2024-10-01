package ar.edu.utn.frc.tup.lc.iv.dtos.common;

import lombok.Data;

@Data
public class ResultadoDto {
    private int orden;
    private String nombre;
    private int votos;
    private Double porcentaje;
}
