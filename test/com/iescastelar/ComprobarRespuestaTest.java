package com.iescastelar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para HU6 - ComprobarRespuesta
 * @author Test
 */
public class ComprobarRespuestaTest {

    private ComprobarRespuesta.Realidad realidad;

    @BeforeEach
    void setUp() {
        realidad = new ComprobarRespuesta.Realidad(
                1,
                "El 90% de los hogares españoles tiene acceso a internet",
                "INE 2023",
                "Solo el 10% de los hogares tiene internet",
                "Fuente falsa"
        );
    }

    // ── Test 1: respuesta correcta suma punto ──────────────────────────────
    @Test
    void testRespuestaCorrectaSumaPunto() {
        Scanner sc = new Scanner(new ByteArrayInputStream("1\n".getBytes()));
        Integer resultado = ComprobarRespuesta.comprobarRespuesta(sc, 1, realidad, 0);
        assertEquals(1, resultado, "Respuesta correcta debe sumar 1 punto");
    }

    // ── Test 2: respuesta incorrecta no suma punto ─────────────────────────
    @Test
    void testRespuestaIncorrectaNoSumaPunto() {
        Scanner sc = new Scanner(new ByteArrayInputStream("2\n".getBytes()));
        Integer resultado = ComprobarRespuesta.comprobarRespuesta(sc, 1, realidad, 0);
        assertEquals(0, resultado, "Respuesta incorrecta no debe sumar punto");
    }

    // ── Test 3: puntuación acumulada correctamente ─────────────────────────
    @Test
    void testPuntuacionAcumulada() {
        Scanner sc = new Scanner(new ByteArrayInputStream("1\n".getBytes()));
        Integer resultado = ComprobarRespuesta.comprobarRespuesta(sc, 1, realidad, 5);
        assertEquals(6, resultado, "Debe acumular la puntuacion previa");
    }

    // ── Test 4: entrada inválida y luego válida ────────────────────────────
    @Test
    void testEntradaInvalidaLuegoValida() {
        // Escribe 0 (inválido), luego 5 (inválido), luego 2 (válido)
        Scanner sc = new Scanner(new ByteArrayInputStream("0\n5\n2\n".getBytes()));
        Integer resultado = ComprobarRespuesta.comprobarRespuesta(sc, 2, realidad, 0);
        assertEquals(1, resultado, "Debe aceptar la respuesta valida tras entradas invalidas");
    }

    // ── Test 5: texto no numérico y luego válido ───────────────────────────
    @Test
    void testTextoNoNumericoLuegoValido() {
        Scanner sc = new Scanner(new ByteArrayInputStream("abc\n3\n".getBytes()));
        Integer resultado = ComprobarRespuesta.comprobarRespuesta(sc, 3, realidad, 2);
        assertEquals(3, resultado, "Debe recuperarse de entrada no numerica");
    }

    // ── Test 6: opción correcta = 2 ───────────────────────────────────────
    @Test
    void testOpcionCorrecta2() {
        Scanner sc = new Scanner(new ByteArrayInputStream("2\n".getBytes()));
        Integer resultado = ComprobarRespuesta.comprobarRespuesta(sc, 2, realidad, 3);
        assertEquals(4, resultado, "Debe sumar punto cuando la opcion correcta es la 2");
    }

    // ── Test 7: opción correcta = 3 ───────────────────────────────────────
    @Test
    void testOpcionCorrecta3() {
        Scanner sc = new Scanner(new ByteArrayInputStream("3\n".getBytes()));
        Integer resultado = ComprobarRespuesta.comprobarRespuesta(sc, 3, realidad, 0);
        assertEquals(1, resultado, "Debe sumar punto cuando la opcion correcta es la 3");
    }
}
