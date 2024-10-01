package ar.edu.utn.frc.tup.lc.iv.dtos.common;

import lombok.Data;

import java.util.List;

@Data
public class ResultadoResponseDto {
    private String distrito;
    private String seccion;
    private List<ResultadoDto> resultados;
}