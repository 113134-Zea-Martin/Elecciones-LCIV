package ar.edu.utn.frc.tup.lc.iv.services.impl;

import ar.edu.utn.frc.tup.lc.iv.dtos.common.CargoGetDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.CargosResponseDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.SeccionDto;
import ar.edu.utn.frc.tup.lc.iv.models.Distrito;
import ar.edu.utn.frc.tup.lc.iv.models.Seccion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class DistritoServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private DistritoServiceImpl distritoServiceImpl;

    private Distrito[] mockDistritos;

    @BeforeEach
    void setUp() {
        mockDistritos = new Distrito[]{
                new Distrito(1, "Distrito A"),
                new Distrito(2, "Distrito B")
        };
    }

    @Test
    void obtenerDistritos() {
        // Configurar el comportamiento del RestTemplate simulado
        when(restTemplate.getForEntity("http://localhost:8080/distritos", Distrito[].class))
                .thenReturn(new ResponseEntity<>(mockDistritos, HttpStatus.OK));

        // Llamar al método que queremos probar
        Distrito[] distritos = distritoServiceImpl.obtenerDistritos();

        // Verificar el comportamiento y resultados esperados
        assertNotNull(distritos);
        assertEquals(2, distritos.length);
        assertEquals("Distrito A", distritos[0].getDistritoNombre());
        assertEquals("Distrito B", distritos[1].getDistritoNombre());
    }

    @Test
    void obtenerDistritosPorNombre() {
        String nombre = "Distrito A";
        when(restTemplate.getForEntity("http://localhost:8080/distritos?distritoNombre=Distrito A", Distrito[].class))
                .thenReturn(new ResponseEntity<>(mockDistritos, HttpStatus.OK));

        Distrito[] distritos = distritoServiceImpl.obtenerDistritosPorNombre(nombre);

        assertNotNull(distritos);
        assertEquals(1, distritos.length);
        assertEquals("Distrito A", distritos[0].getDistritoNombre());
    }


    @Test
    void obtenerCargosPorDistrito() {
        int distritoId = 1;
        CargoGetDto[] mockCargos = {
                new CargoGetDto(1, "Cargo 1", 1),
                new CargoGetDto(2, "Cargo 2", 1)
        };

        Distrito[] mockDistrito = {new Distrito(1, "Distrito A")};

        when(restTemplate.getForEntity("http://localhost:8080/distritos?distritoId=1", Distrito[].class))
                .thenReturn(new ResponseEntity<>(mockDistrito, HttpStatus.OK));

        when(restTemplate.getForEntity("http://localhost:8080/cargos?distritoId=1", CargoGetDto[].class))
                .thenReturn(new ResponseEntity<>(mockCargos, HttpStatus.OK));

        CargosResponseDto response = distritoServiceImpl.obtenerCargosPorDistrito(distritoId);

        assertNotNull(response);
        assertEquals("Distrito A", response.getDistrito().getNombre());
        assertEquals(2, response.getCargos().length);
    }


    @Test
    void obtenerSeccionesPorDistrito() {
        Long distritoId = 1L;
        Seccion[] mockSecciones = {
                new Seccion(1L, "Seccion 1", 1L),
                new Seccion(2L, "Seccion 2", 1L)
        };

        when(restTemplate.getForEntity("http://localhost:8080/secciones?distritoId=1", Seccion[].class))
                .thenReturn(new ResponseEntity<>(mockSecciones, HttpStatus.OK));

        List<SeccionDto> secciones = distritoServiceImpl.obtenerSeccionesPorDistrito(distritoId);

        assertNotNull(secciones);
        assertEquals(2, secciones.size());
        assertEquals("Seccion 1", secciones.get(0).getNombre());
    }


    @Test
    void obtenerSeccionDtoPorDistritoYSeccion() {
    }

   /* @Test
    void obtenerResultado() {
        Long seccionId = 1L;
        Resultado[] mockResultados = {
                new Resultado(135, "Distrito A", "Seccion 1", 100, "NORMAL"),
                new Resultado(132, "Distrito A", "Seccion 1", 200, "NORMAL"),
                new Resultado(0, "Distrito A", "Seccion 1", 50, "EN BLANCO")
        };

        when(restTemplate.getForEntity("http://localhost:8080/resultados?seccionId=1", Resultado[].class))
                .thenReturn(new ResponseEntity<>(mockResultados, HttpStatus.OK));

        ResultadoResponseDto resultado = distritoServiceImpl.obtenerResultado(seccionId);

        assertNotNull(resultado);
        assertEquals("Distrito A", resultado.getDistrito());
        assertEquals(3, resultado.getResultados().size());
        assertEquals("LA LIBERTAD AVANZA", resultado.getResultados().get(0).getNombre());
        assertEquals(100, resultado.getResultados().get(0).getVotos());
    }
    */

}