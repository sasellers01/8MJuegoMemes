package com.iescastelar;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class GestorFicherosTest {

    @Test
    @DisplayName("Test constructor GestorFicheros")
    void testConstructor() {

        GestorFicheros gestor = new GestorFicheros();

        assertNotNull(gestor, "El constructor de GestorFicheros devuelve null");
    }

    @Test
    @DisplayName("Test verificación de ficheros")
    void testVerificarDatos() {

        GestorFicheros gestor = new GestorFicheros();

        boolean resultado = gestor.verificarDatos();

        assertTrue(resultado, "La verificación de datos debería ser correcta");
    }
}