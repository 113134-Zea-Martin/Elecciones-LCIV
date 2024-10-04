package ar.edu.utn.frc.tup.lc.iv.services.impl;

import ar.edu.utn.frc.tup.lc.iv.dtos.common.*;
import ar.edu.utn.frc.tup.lc.iv.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DistritoServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private DistritoServiceImpl distritoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void obtenerDistritos() {
        Distrito[] expectedDistritos = {new Distrito(1, "Distrito A"), new Distrito(2, "Distrito B")};
        when(restTemplate.getForEntity(anyString(), eq(Distrito[].class))).thenReturn(new ResponseEntity<>(expectedDistritos, HttpStatus.OK));

        Distrito[] actualDistritos = distritoService.obtenerDistritos();

        assertNotNull(actualDistritos);
        assertEquals(expectedDistritos.length, actualDistritos.length);
        verify(restTemplate, times(1)).getForEntity(anyString(), eq(Distrito[].class));
    }

    @Test
    void fallback() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> distritoService.fallback(new Exception()));
        assertEquals("Error manejado en el fallback", exception.getMessage());
    }

    @Test
    void obtenerDistritosPorNombre() {
        Distrito[] expectedDistritos = {new Distrito(1, "Distrito A")};
        when(restTemplate.getForEntity(anyString(), eq(Distrito[].class))).thenReturn(new ResponseEntity<>(expectedDistritos, HttpStatus.OK));

        Distrito[] actualDistritos = distritoService.obtenerDistritosPorNombre("Distrito A");

        assertNotNull(actualDistritos);
        assertEquals(expectedDistritos[0].getDistritoNombre(), actualDistritos[0].getDistritoNombre());
        verify(restTemplate, times(1)).getForEntity(anyString(), eq(Distrito[].class));
    }

    @Test
    void obtenerCargosPorDistrito() {
        Distrito[] expectedDistritos = {new Distrito(1, "Distrito A")};
        CargoGetDto[] expectedCargos = {new CargoGetDto(1, "Cargo A", 1)};

        when(restTemplate.getForEntity(anyString(), eq(Distrito[].class))).thenReturn(new ResponseEntity<>(expectedDistritos, HttpStatus.OK));
        when(restTemplate.getForEntity(anyString(), eq(CargoGetDto[].class))).thenReturn(new ResponseEntity<>(expectedCargos, HttpStatus.OK));

        CargosResponseDto actualCargos = distritoService.obtenerCargosPorDistrito(1);

        assertNotNull(actualCargos);
        assertEquals(expectedCargos.length, actualCargos.getCargos().length);
        assertEquals(expectedCargos[0].getCargoNombre(), actualCargos.getCargos()[0].getNombre());
        verify(restTemplate, times(2)).getForEntity(anyString(), any());
    }

    @Test
    void obtenerSeccionesPorDistrito() {
        Seccion[] expectedSecciones = {new Seccion(1L, "Seccion A", 1L)};
        when(restTemplate.getForEntity(anyString(), eq(Seccion[].class))).thenReturn(new ResponseEntity<>(expectedSecciones, HttpStatus.OK));

        List<SeccionDto> actualSecciones = distritoService.obtenerSeccionesPorDistrito(1L);

        assertNotNull(actualSecciones);
        assertEquals(expectedSecciones.length, actualSecciones.size());
        assertEquals(expectedSecciones[0].getSeccionNombre(), actualSecciones.get(0).getNombre());
        verify(restTemplate, times(1)).getForEntity(anyString(), eq(Seccion[].class));
    }

    @Test
    void obtenerSeccionDtoPorDistritoYSeccion() {
        Seccion[] expectedSecciones = {new Seccion(1L, "Seccion A", 1L)};
        when(restTemplate.getForEntity(anyString(), eq(Seccion[].class))).thenReturn(new ResponseEntity<>(expectedSecciones, HttpStatus.OK));

        SeccionDto actualSeccionDto = distritoService.obtenerSeccionDtoPorDistritoYSeccion(1L, 1L);

        assertNotNull(actualSeccionDto);
        assertEquals(expectedSecciones[0].getSeccionNombre(), actualSeccionDto.getNombre());
        verify(restTemplate, times(1)).getForEntity(anyString(), eq(Seccion[].class));
    }

    @Test
    void obtenerResultado() {
        // Creamos el objeto Resultado y usamos los setters para establecer sus valores
        Resultado resultado = new Resultado();
        resultado.setId(1L);
        resultado.setEleccionTipo("Tipo");
        resultado.setRecuentoTipo("Recuento");
        resultado.setPadronTipo("Padron");
        resultado.setDistritoId(1L);
        resultado.setDistritoNombre("Distrito A");
        resultado.setSeccionProvincialId(1L);
        resultado.setSeccionProvincialNombre("Seccion A");
        resultado.setSeccionId(1L);
        resultado.setSeccionNombre("Seccion A");
        resultado.setCircuitoId("1");
        resultado.setCircuitoNombre("Circuito A");
        resultado.setMesaId(1L);
        resultado.setMesaTipo("Tipo");
        resultado.setMesaElectores(100L);
        resultado.setCargoId(1L);
        resultado.setCargoNombre("Cargo A");
        resultado.setAgrupacionId(135);
        resultado.setAgrupacionNombre("Agrupacion A");
        resultado.setListaNumero("1");
        resultado.setListaNombre("Lista A");
        resultado.setVotosTipo("EN BLANCO");
        resultado.setVotosCantidad(50);
        resultado.setAño(2023);

        // Simulamos la respuesta de la API con el array de resultados
        Resultado[] expectedResultados = {resultado};

        when(restTemplate.getForEntity(anyString(), eq(Resultado[].class)))
                .thenReturn(new ResponseEntity<>(expectedResultados, HttpStatus.OK));

        // Llamamos al método del servicio que estamos probando
        ResultadoResponseDto actualResultado = distritoService.obtenerResultado(1L);

        // Verificamos que el resultado no sea null y que los valores sean los esperados
        assertNotNull(actualResultado);
        assertEquals("Distrito A", actualResultado.getDistrito());
        assertEquals("Seccion A", actualResultado.getSeccion());
        verify(restTemplate, times(1)).getForEntity(anyString(), eq(Resultado[].class));
    }

}
