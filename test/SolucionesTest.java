package com.iescastelar;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class SolucionesTest {

    @Test
    @DisplayName("Test carga de soluciones")
    void testCargaSoluciones() {

        Soluciones soluciones = new Soluciones();

        soluciones.cargarSoluciones();

        Integer realidadId = soluciones.getRealidadId(1);

        assertNotNull(realidadId, "No se encontró realidad para el meme");
    }
}