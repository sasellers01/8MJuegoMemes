package com.iescastelar;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de pruebas para la clase Realidad.
 * 
 * Comprueba el correcto funcionamiento del constructor,
 * los métodos getter y el método toString().
 * 
 * @author Sergio S.
 */
class RealidadTest {

    @Test
    @DisplayName("Test constructor válido")
    void testConstructorValido() {

        Realidad realidad = new Realidad(1, "Texto de prueba", "Fuente de prueba");

        assertNotNull(realidad, "El objeto Realidad es null");
    }

    @Test
    @DisplayName("Test getters")
    void testGetters() {

        Realidad realidad = new Realidad(1, "Texto de prueba", "Fuente de prueba");

        assertEquals(1, realidad.getId(), "El ID no es correcto");
        assertEquals("Texto de prueba", realidad.getTexto(), "El texto no es correcto");
        assertEquals("Fuente de prueba", realidad.getFuente(), "La fuente no es correcta");
    }

    @Test
    @DisplayName("Test toString")
    void testToString() {

        Realidad realidad = new Realidad(1, "Texto de prueba", "Fuente de prueba");

        String esperado = "Texto de prueba (Fuente: Fuente de prueba)";
        String resultado = realidad.toString();

        assertEquals(esperado, resultado, "El toString no es correcto");
    }

    @Test
    @DisplayName("Test valores no nulos")
    void testValoresNoNulos() {

        Realidad realidad = new Realidad(1, "Texto", "Fuente");

        assertNotNull(realidad.getId());
        assertNotNull(realidad.getTexto());
        assertNotNull(realidad.getFuente());
    }
}