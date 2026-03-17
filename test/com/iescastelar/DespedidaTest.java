package com.iescastelar;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.lang.reflect.*;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la clase Despedida.
 *
 * Dependencias necesarias en pom.xml (Maven):
 *
 *   <dependency>
 *       <groupId>org.junit.jupiter</groupId>
 *       <artifactId>junit-jupiter</artifactId>
 *       <version>5.10.2</version>
 *       <scope>test</scope>
 *   </dependency>
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DespedidaTest {

    @TempDir
    Path tempDir;

    private PrintStream originalOut;
    private ByteArrayOutputStream baos;

    // -----------------------------------------------------------------------
    // Configuración: redirigir ARCHIVO al directorio temporal y capturar salida
    // -----------------------------------------------------------------------

    @BeforeEach
    void setUp() throws Exception {
        // Redirigir el campo ARCHIVO al directorio temporal
        setField("ARCHIVO", tempDir.resolve("mejores.txt").toString());

        // Capturar System.out
        originalOut = System.out;
        baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    // -----------------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------------

    private void setField(String fieldName, String valor) throws Exception {
        Field f = Despedida.class.getDeclaredField(fieldName);
        f.setAccessible(true);
        Field mod = Field.class.getDeclaredField("modifiers");
        mod.setAccessible(true);
        mod.setInt(f, f.getModifiers() & ~Modifier.FINAL);
        f.set(null, valor);
    }

    private void escribirRanking(String... lineas) throws Exception {
        Path archivo = tempDir.resolve("mejores.txt");
        Files.write(archivo, Arrays.asList(lineas));
    }

    private String salida() {
        return baos.toString();
    }

    // -----------------------------------------------------------------------
    // TESTS: mostrarRankingYDespedirse()
    // -----------------------------------------------------------------------

    @Test
    @Order(1)
    @DisplayName("Muestra el encabezado TOP 3 siempre")
    void testMuestraEncabezado() {
        Despedida.mostrarRankingYDespedirse();
        assertTrue(salida().contains("TOP 3"), "Debe mostrar el encabezado TOP 3");
    }

    @Test
    @Order(2)
    @DisplayName("Sin registros muestra '(sin registros aún)'")
    void testSinRegistros() {
        Despedida.mostrarRankingYDespedirse();
        assertTrue(salida().contains("sin registros"), "Debe indicar que no hay registros");
    }

    @Test
    @Order(3)
    @DisplayName("Con registros muestra nombres y puntuaciones")
    void testConRegistros() throws Exception {
        escribirRanking("Alice;300", "Bob;200", "Carol;100");

        Despedida.mostrarRankingYDespedirse();

        String s = salida();
        assertTrue(s.contains("Alice"), "Debe mostrar el nombre Alice");
        assertTrue(s.contains("300"),   "Debe mostrar la puntuación 300");
        assertTrue(s.contains("Bob"),   "Debe mostrar el nombre Bob");
        assertTrue(s.contains("200"),   "Debe mostrar la puntuación 200");
        assertTrue(s.contains("Carol"), "Debe mostrar el nombre Carol");
        assertTrue(s.contains("100"),   "Debe mostrar la puntuación 100");
    }

    @Test
    @Order(4)
    @DisplayName("Muestra el mensaje de despedida")
    void testMensajeDespedida() {
        Despedida.mostrarRankingYDespedirse();
        assertTrue(salida().contains("Gracias por jugar") || salida().contains("Hasta"),
                "Debe mostrar un mensaje de despedida");
    }

    @Test
    @Order(5)
    @DisplayName("El mensaje de despedida aparece DESPUÉS del ranking")
    void testDespedidaApareceDespuesDelRanking() throws Exception {
        escribirRanking("Alice;300");

        Despedida.mostrarRankingYDespedirse();

        String s = salida();
        int posRanking   = s.indexOf("Alice");
        int posDespedida = s.indexOf("Gracias");

        assertTrue(posRanking < posDespedida,
                "El ranking debe aparecer antes que la despedida");
    }

    @Test
    @Order(6)
    @DisplayName("Archivo inexistente no lanza excepción y muestra '(sin registros aún)'")
    void testArchivoInexistente() throws Exception {
        setField("ARCHIVO", tempDir.resolve("no_existe.txt").toString());

        assertDoesNotThrow(() -> Despedida.mostrarRankingYDespedirse());
        assertTrue(salida().contains("sin registros"));
    }

    @Test
    @Order(7)
    @DisplayName("Ignora líneas malformadas y muestra solo las válidas")
    void testLineasMalformadas() throws Exception {
        escribirRanking("Alice;300", "linea_invalida", "", "Bob;200");

        Despedida.mostrarRankingYDespedirse();

        String s = salida();
        assertTrue(s.contains("Alice"));
        assertTrue(s.contains("Bob"));
        assertFalse(s.contains("linea_invalida"));
    }

    @Test
    @Order(8)
    @DisplayName("Las posiciones se numeran correctamente (1. 2. 3.)")
    void testNumeracionPosiciones() throws Exception {
        escribirRanking("Alice;300", "Bob;200", "Carol;100");

        Despedida.mostrarRankingYDespedirse();

        String s = salida();
        assertTrue(s.contains("1."), "Debe mostrar la posición 1.");
        assertTrue(s.contains("2."), "Debe mostrar la posición 2.");
        assertTrue(s.contains("3."), "Debe mostrar la posición 3.");
    }

    @Test
    @Order(9)
    @DisplayName("Con un solo registro muestra ese registro y se despide")
    void testUnSoloRegistro() throws Exception {
        escribirRanking("Jugador1;5");

        Despedida.mostrarRankingYDespedirse();

        String s = salida();
        assertTrue(s.contains("Jugador1"));
        assertTrue(s.contains("5"));
        assertTrue(s.contains("Gracias") || s.contains("Hasta"));
    }
}
