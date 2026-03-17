package com.iescastelar;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de pruebas para la clase Soluciones.
 * 
 * Comprueba el correcto funcionamiento de la carga de datos
 * desde el XML y la relación entre memes y realidades.
 * 
 * @author Sergio S.
 */
class SolucionesTest {
	
	@Test
	@DisplayName("Test constructor")
	void testConstructor() {
		
		Soluciones soluciones = new Soluciones();
		
		assertNotNull(soluciones, "El objeto Soluciones es null");
	}
	
	@Test
	@DisplayName("Test carga de soluciones")
	void testCargaSoluciones() {
		
		Soluciones soluciones = new Soluciones();
		
		soluciones.cargarSoluciones();
		
		Integer realidadId = soluciones.getRealidadId(1);
		
		assertNotNull(realidadId, "No se encontró realidad para el meme 1");
	}
	
	@Test
	@DisplayName("Test varias soluciones")
	void testVariasSoluciones() {
		
		Soluciones soluciones = new Soluciones();
		
		soluciones.cargarSoluciones();
		
		// Probamos varios IDs (ajústalos si hace falta según tu XML)
		Integer r1 = soluciones.getRealidadId(1);
		Integer r2 = soluciones.getRealidadId(2);
		
		assertNotNull(r1, "Meme 1 sin solución");
		assertNotNull(r2, "Meme 2 sin solución");
	}
	
	@Test
	@DisplayName("Test meme inexistente")
	void testMemeInexistente() {
		
		Soluciones soluciones = new Soluciones();
		
		soluciones.cargarSoluciones();
		
		Integer realidadId = soluciones.getRealidadId(999);
		
		assertNull(realidadId, "Debería devolver null para un meme inexistente");
	}
	
	@Test
	@DisplayName("Test consistencia de datos")
	void testConsistencia() {
		
		Soluciones soluciones = new Soluciones();
		
		soluciones.cargarSoluciones();
		
		Integer realidadId = soluciones.getRealidadId(1);
		
		assertTrue(realidadId > 0, "El ID de la realidad debe ser mayor que 0");
	}
}