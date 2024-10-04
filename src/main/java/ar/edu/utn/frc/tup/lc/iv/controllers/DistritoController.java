package ar.edu.utn.frc.tup.lc.iv.controllers;

import ar.edu.utn.frc.tup.lc.iv.dtos.common.CargosResponseDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.ResultadoResponseDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.SeccionDto;
import ar.edu.utn.frc.tup.lc.iv.models.Distrito;
import ar.edu.utn.frc.tup.lc.iv.services.DistritoService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping()
public class DistritoController {

    @Autowired
    private DistritoService distritoService;



    @GetMapping("/distritos")
    public ResponseEntity<Distrito[]> getDistritos(@RequestParam(required = false) String distrito_nombre) {

        Distrito[] distritos;

        if (distrito_nombre == null) {
            distritos = distritoService.obtenerDistritos();
        } else {
           distritos = distritoService.obtenerDistritosPorNombre(distrito_nombre);
        }
        return ResponseEntity.ok(distritos);
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return new ResponseEntity<>("Error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/cargos")
    public ResponseEntity<CargosResponseDto> getCargosPorDistrito(@RequestParam int distrito_id){
        CargosResponseDto cargosResponseDto;
        cargosResponseDto = distritoService.obtenerCargosPorDistrito(distrito_id);
        return ResponseEntity.ok(cargosResponseDto);
    }

    @GetMapping("/secciones")
    public ResponseEntity<List<SeccionDto>> getSecciones(@RequestParam Long distrito_id, @RequestParam(required = false) Long seccion_id){
        List<SeccionDto> secciones = new ArrayList<>();
        if (seccion_id == null) {
            secciones = distritoService.obtenerSeccionesPorDistrito(distrito_id);
        } else {
            SeccionDto seccionDto = distritoService.obtenerSeccionDtoPorDistritoYSeccion(distrito_id, seccion_id);
            secciones.add(seccionDto);
        }
        return ResponseEntity.ok(secciones);
    }

    @GetMapping("/resultados")
    public ResponseEntity<ResultadoResponseDto> getResultados(@RequestParam Long seccionId){
        ResultadoResponseDto resultadoResponseDto = distritoService.obtenerResultado(seccionId);
        return ResponseEntity.ok(resultadoResponseDto);
    }

}
