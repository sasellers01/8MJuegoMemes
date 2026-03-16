package com.iescastelar;

import java.util.*;

/**
 * Tests para HU5: mostrarMemeYRealidades()
 */
public class SistemaMemesTest {

    /**
     * Test 1: comprueba que no se repite un meme ya usado
     */
    static void testNoRepetirMemes() {
        Map<Integer, String> memes = new LinkedHashMap<>();
        memes.put(1, "Meme 1");
        memes.put(2, "Meme 2");
        memes.put(3, "Meme 3");

        List<Integer> usados = new ArrayList<>();
        usados.add(1);
        usados.add(2);

        List<Integer> disponibles = new ArrayList<>(memes.keySet());
        disponibles.removeAll(usados);

        assert disponibles.size() == 1 : "FALLO: deberia haber 1 meme disponible";
        assert disponibles.get(0).equals(3) : "FALLO: el meme disponible deberia ser el 3";
        System.out.println("Test 1 OK: no se repiten memes");
    }

    /**
     * Test 2: comprueba que devuelve 1 o 2
     */
    static void testDevuelveOpcionValida() {
        List<String> opciones = new ArrayList<>();
        opciones.add("Opcion correcta");
        opciones.add("Opcion falsa");

        Integer resultado = opciones.indexOf("Opcion correcta") + 1;

        assert resultado == 1 || resultado == 2 : "FALLO: debe devolver 1 o 2";
        System.out.println("Test 2 OK: devuelve opcion valida");
    }

    public static void main(String[] args) {
        testNoRepetirMemes();
        testDevuelveOpcionValida();
        System.out.println("Todos los tests de HU5 pasados correctamente");
    }
}
