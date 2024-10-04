package ar.edu.utn.frc.tup.lc.iv.services.impl;

import ar.edu.utn.frc.tup.lc.iv.dtos.common.*;
import ar.edu.utn.frc.tup.lc.iv.models.Cargo;
import ar.edu.utn.frc.tup.lc.iv.models.Distrito;
import ar.edu.utn.frc.tup.lc.iv.models.Resultado;
import ar.edu.utn.frc.tup.lc.iv.models.Seccion;
import ar.edu.utn.frc.tup.lc.iv.services.DistritoService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class DistritoServiceImpl implements DistritoService {

    //private final String URL = "http://java-api:8080";
    private final String URL = "http://localhost:8080";

    @Autowired
    private RestTemplate restTemplate;


    private static final String RESILIENCE4J_INSTANCE_NAME = "microCircuitBreaker";
    private static final String FALLBACK_METHOD = "fallback";
    private Integer counter = 0;

    @Override
    @CircuitBreaker(name = RESILIENCE4J_INSTANCE_NAME, fallbackMethod = FALLBACK_METHOD)
    public Distrito[] obtenerDistritos() {
        counter++;
        System.out.println("Execution N° " + counter + " - Calling micro B");
        Distrito[] distritos = restTemplate.getForEntity(URL + "/distritos", Distrito[].class).getBody();
        return distritos;
    }

    public Distrito[] fallback(Exception ex) {
        counter++;
        System.out.println("Execution N° " + counter + " - FallBack B");
        //return ResponseEntity.status(503).body("Soy un Circuit-Breaker funcionando :D");

        throw new RuntimeException("Error manejado en el fallback");
        //throw new ResponseStatusException(HttpStatusCode.valueOf(503), "Soy un Circuit-Breaker funcionando :D", ex);
    }


    @Override
    public Distrito[] obtenerDistritosPorNombre(String nombre) {
        Distrito[] distritos = restTemplate.getForEntity(URL + "/distritos?distritoNombre=" + nombre, Distrito[].class).getBody();
        return distritos;
    }

    @Override
    public CargosResponseDto obtenerCargosPorDistrito(int distrito_id) {
        Distrito[] distrito = restTemplate.getForEntity(URL + "/distritos?distritoId=" + distrito_id, Distrito[].class).getBody();

        DistritoDto distritoDto = new DistritoDto();
        distritoDto.setId(distrito[0].getDistritoId());
        distritoDto.setNombre(distrito[0].getDistritoNombre());


        CargoGetDto[] cargoGetDtos = restTemplate.getForEntity(URL + "/cargos?distritoId=" + distrito_id, CargoGetDto[].class).getBody();

        Cargo[] cargos = new Cargo[cargoGetDtos.length];
        int i = 0;

        for (CargoGetDto cargoGetDto : cargoGetDtos) {
            Cargo cargo = new Cargo();
            cargo.setId(cargoGetDto.getCargoId());
            cargo.setNombre(cargoGetDto.getCargoNombre());
            cargos[i] = cargo;
            i++;
        }

        CargosResponseDto cargosResponseDto = new CargosResponseDto();
        cargosResponseDto.setDistrito(distritoDto);
        cargosResponseDto.setCargos(cargos);

        return cargosResponseDto;
    }

    @Override
    public List<SeccionDto> obtenerSeccionesPorDistrito(Long distrito_id) {
        Seccion[] secciones = restTemplate.getForEntity(URL + "/secciones?distritoId=" + distrito_id, Seccion[].class).getBody();
        List<SeccionDto> seccionesDto = new ArrayList<>();

        for (Seccion seccion : secciones) {
            SeccionDto seccionDto = new SeccionDto();
            seccionDto.setId(seccion.getSeccionId());
            seccionDto.setNombre(seccion.getSeccionNombre());
            seccionesDto.add(seccionDto);
        }
        return seccionesDto;
    }

    @Override
    public SeccionDto obtenerSeccionDtoPorDistritoYSeccion(Long distrito_id, Long seccion_id) {
        Seccion[] seccion = restTemplate.getForEntity(URL + "/secciones?seccionId=" + seccion_id + "&distritoId=" + distrito_id, Seccion[].class).getBody();
        SeccionDto seccionDto = new SeccionDto();
        seccionDto.setId(seccion[0].getSeccionId());
        seccionDto.setNombre(seccion[0].getSeccionNombre());
        return seccionDto;
    }

    @Override
    public ResultadoResponseDto obtenerResultado(Long seccionId) {
        Resultado[] resultados = restTemplate.getForEntity(URL + "/resultados?seccionId=" + seccionId, Resultado[].class).getBody();

        int LLA = 0;
        int JPC = 0;
        int HPNP = 0;
        int ULP = 0;
        int IZQ = 0;
        int Blanco = 0;
        int Nulo = 0;
        int Impugnado = 0;
        int Recurrido = 0;

        for (Resultado resultado : resultados) {
            if (resultado.getAgrupacionId() == 135) {
                LLA += resultado.getVotosCantidad();
            } else if (resultado.getAgrupacionId() == 132) {
                JPC += resultado.getVotosCantidad();
            } else if (resultado.getAgrupacionId() == 133) {
                HPNP += resultado.getVotosCantidad();
            } else if (resultado.getAgrupacionId() == 134) {
                ULP += resultado.getVotosCantidad();
            } else if (resultado.getAgrupacionId() == 136) {
                IZQ += resultado.getVotosCantidad();
            } else if (resultado.getAgrupacionId() == 0) {
                if (Objects.equals(resultado.getVotosTipo(), "EN BLANCO")) {
                    Blanco += resultado.getVotosCantidad();
                } else if (Objects.equals(resultado.getVotosTipo(), "NULO")) {
                    Nulo += resultado.getVotosCantidad();
                } else if (Objects.equals(resultado.getVotosTipo(), "IMPUGNADO")) {
                    Impugnado += resultado.getVotosCantidad();
                } else if (Objects.equals(resultado.getVotosTipo(), "RECURRIDO")) {
                    Recurrido += resultado.getVotosCantidad();
                }
            }
        }

        String[] nombre = {"LA LIBERTAD AVANZA", "JUNTOS POR EL CAMBIO", "HACEMOS POR NUESTRO PAIS", "UNION POR LA PATRIA", "FRENTE DE IZQUIERDA Y DE TRABAJADORES - UNIDAD",
                "EN BLANCO", "NULO", "IMPUGNADO", "RECURRIDO"};
        int[] votos = new int[]{LLA, JPC, HPNP, ULP, IZQ, Blanco, Nulo, Impugnado, Recurrido};

        int votosTotales = 0;
        for (int i = 0; i < 9; i++) {
            votosTotales += votos[i];
        }

        List<ResultadoDto> resultadoDtos = new ArrayList<>();

        for (int i = 1; i <= 9; i++) {
            ResultadoDto resultadoDto = new ResultadoDto();
            resultadoDto.setOrden(i);
            resultadoDto.setNombre(nombre[i - 1]);
            resultadoDto.setVotos(votos[i - 1]);
            resultadoDto.setPorcentaje((double) resultadoDto.getVotos() / votosTotales);
            resultadoDtos.add(resultadoDto);
        }

        ResultadoResponseDto resultadoResponseDto = new ResultadoResponseDto();
        resultadoResponseDto.setDistrito(resultados[0].getDistritoNombre());
        resultadoResponseDto.setSeccion(resultados[0].getSeccionNombre());
        resultadoResponseDto.setResultados(resultadoDtos);

        return resultadoResponseDto;
    }


}
