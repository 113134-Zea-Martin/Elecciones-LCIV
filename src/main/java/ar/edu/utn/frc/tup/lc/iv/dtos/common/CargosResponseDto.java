package ar.edu.utn.frc.tup.lc.iv.dtos.common;


import ar.edu.utn.frc.tup.lc.iv.models.Cargo;
import ar.edu.utn.frc.tup.lc.iv.models.Distrito;
import lombok.Data;

@Data
public class CargosResponseDto {
    private DistritoDto distrito;
    private Cargo[] cargos;
}