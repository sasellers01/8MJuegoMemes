package com.iescastelar;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

class MainTest {

    @Test
    @DisplayName("Test ejecución del programa principal")
    void testMain() {

        PrintStream salidaConsola = System.out;

        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        PrintStream salidaTest = new PrintStream(salida);

        System.setOut(salidaTest);

        try {
            Main.main(new String[]{});
        } catch (Exception e) {
            fail("El programa lanzó una excepción inesperada");
        }

        System.setOut(salidaConsola);

        String output = salida.toString();

        assertTrue(output.contains("INICIO"), "El programa no se inicia correctamente");
    }
}