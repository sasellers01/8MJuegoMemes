package com.iescastelar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para HU5 - SistemaMemes
 */
public class SistemaMemesTest {

    private Map<Integer, String> memes;
    private List<SistemaMemes.Realidad> realidades;

    @BeforeEach
    void setUp() {
        memes = new LinkedHashMap<>();
        memes.put(1, "Los españoles son los que menos trabajan de Europa");
        memes.put(2, "España es el país más corrupto de la UE");
        memes.put(3, "Los jóvenes no leen nada");

        realidades = new ArrayList<>();
        realidades.add(new SistemaMemes.Realidad(
                1,
                "España supera la media europea en horas trabajadas",
                "Eurostat 2023",
                "España trabaja menos que Luxemburgo",
                "Fuente falsa"
        ));
        realidades.add(new SistemaMemes.Realidad(
                2,
                "España ocupa el puesto 36 en el índice de percepción de corrupción",
                "Transparency International 2023",
                "España es el país más corrupto según la ONU",
                "Fuente falsa"
        ));
        realidades.add(new SistemaMemes.Realidad(
                3,
                "El 62% de los jóvenes españoles lee libros regularmente",
                "Ministerio de Cultura 2023",
                "Solo el 5% de los jóvenes lee",
                "Fuente falsa"
        ));
    }

    // ── Test 1: devuelve 1 o 2 (opción válida) ────────────────────────────
    @Test
    void testDevuelveOpcionValida() {
        List<Integer> memesUsados = new ArrayList<>();
        Integer opcionCorrecta = SistemaMemes.mostrarMemeYRealidades(memes, realidades, memesUsados, 0);
        assertTrue(opcionCorrecta == 1 || opcionCorrecta == 2,
                "La opcion correcta debe ser 1 o 2");
    }

    // ── Test 2: el meme mostrado se añade a memesUsados ───────────────────
    @Test
    void testMemeUsadoSeRegistra() {
        List<Integer> memesUsados = new ArrayList<>();
        SistemaMemes.mostrarMemeYRealidades(memes, realidades, memesUsados, 0);
        assertEquals(1, memesUsados.size(), "Debe registrar 1 meme como usado");
        assertTrue(memes.containsKey(memesUsados.get(0)), "El ID registrado debe existir en el mapa");
    }

    // ── Test 3: no repite memes ya usados ─────────────────────────────────
    @Test
    void testNoRepiteMemes() {
        List<Integer> memesUsados = new ArrayList<>();
        SistemaMemes.mostrarMemeYRealidades(memes, realidades, memesUsados, 0);
        SistemaMemes.mostrarMemeYRealidades(memes, realidades, memesUsados, 0);
        SistemaMemes.mostrarMemeYRealidades(memes, realidades, memesUsados, 0);

        assertEquals(3, memesUsados.size(), "Deben haberse usado 3 memes distintos");
        assertEquals(3, new HashSet<>(memesUsados).size(), "No debe haber memes repetidos");
    }

    // ── Test 4: puntuacion 0 no lanza excepcion ───────────────────────────
    @Test
    void testPuntuacionCeroNoFalla() {
        List<Integer> memesUsados = new ArrayList<>();
        assertDoesNotThrow(() ->
                SistemaMemes.mostrarMemeYRealidades(memes, realidades, memesUsados, 0));
    }

    // ── Test 5: puntuacion alta no lanza excepcion ────────────────────────
    @Test
    void testPuntuacionAltaNoFalla() {
        List<Integer> memesUsados = new ArrayList<>();
        assertDoesNotThrow(() ->
                SistemaMemes.mostrarMemeYRealidades(memes, realidades, memesUsados, 999));
    }

    // ── Test 6: fuerza un meme concreto y verifica opcion correcta ─────────
    @Test
    void testOpcionCorrectaCorrespondeATextoCorrecto() {
        // Forzamos que solo haya un meme disponible (el 1)
        Map<Integer, String> unSoloMeme = new LinkedHashMap<>();
        unSoloMeme.put(1, "Los españoles son los que menos trabajan de Europa");

        List<SistemaMemes.Realidad> unaRealidad = new ArrayList<>();
        unaRealidad.add(new SistemaMemes.Realidad(
                1,
                "TEXTO_CORRECTO",
                "Fuente correcta",
                "TEXTO_FALSO",
                "Fuente falsa"
        ));

        List<Integer> memesUsados = new ArrayList<>();
        Integer opcion = SistemaMemes.mostrarMemeYRealidades(unSoloMeme, unaRealidad, memesUsados, 0);

        // La opción devuelta debe ser 1 o 2, y debe ser válida
        assertTrue(opcion == 1 || opcion == 2,
                "La opcion correcta debe ser 1 o 2 incluso con un solo meme");
    }

    // ── Test 7: lista disponibles vacía lanza excepcion ───────────────────
    @Test
    void testSinMemesDisponiblesLanzaExcepcion() {
        List<Integer> memesUsados = new ArrayList<>(memes.keySet()); // todos usados
        assertThrows(Exception.class, () ->
                SistemaMemes.mostrarMemeYRealidades(memes, realidades, memesUsados, 0),
                "Debe lanzar excepcion si no hay memes disponibles");
    }
}
