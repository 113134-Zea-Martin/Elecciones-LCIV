package ar.edu.utn.frc.tup.lc.iv.services;

import ar.edu.utn.frc.tup.lc.iv.dtos.common.CargosResponseDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.ResultadoResponseDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.SeccionDto;
import ar.edu.utn.frc.tup.lc.iv.models.Distrito;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DistritoService {
    Distrito[] obtenerDistritos();
    Distrito[] obtenerDistritosPorNombre(String nombre);
    CargosResponseDto obtenerCargosPorDistrito(int distrito_id);
    List<SeccionDto> obtenerSeccionesPorDistrito(Long distrito_id);
    SeccionDto obtenerSeccionDtoPorDistritoYSeccion(Long distrito_id, Long seccion_id);
    ResultadoResponseDto obtenerResultado(Long seccionId);
}
