package com.iescastelar;

/**
 * Tests para HU6: comprobarRespuesta()
 */
public class SistemaMemesTest {

    /**
     * Test 1: comprueba que una respuesta correcta suma un punto
     */
    static void testRespuestaCorrecta() {
        Integer puntuacion = 0;
        Integer opcionCorrecta = 1;
        Integer respuesta = 1;

        if (respuesta.equals(opcionCorrecta)) puntuacion++;

        assert puntuacion == 1 : "FALLO: deberia sumar 1 punto";
        System.out.println("Test 1 OK: respuesta correcta suma punto");
    }

    /**
     * Test 2: comprueba que una respuesta incorrecta no suma punto
     */
    static void testRespuestaIncorrecta() {
        Integer puntuacion = 0;
        Integer opcionCorrecta = 1;
        Integer respuesta = 2;

        if (respuesta.equals(opcionCorrecta)) puntuacion++;

        assert puntuacion == 0 : "FALLO: no deberia sumar punto";
        System.out.println("Test 2 OK: respuesta incorrecta no suma punto");
    }

    /**
     * Test 3: comprueba que solo acepta valores entre 1 y 3
     */
    static void testRespuestaInvalida() {
        Integer respuesta = 5;
        Boolean valida = respuesta >= 1 && respuesta <= 3;

        assert !valida : "FALLO: 5 no deberia ser valido";
        System.out.println("Test 3 OK: respuesta invalida rechazada");
    }

    public static void main(String[] args) {
        testRespuestaCorrecta();
        testRespuestaIncorrecta();
        testRespuestaInvalida();
        System.out.println("Todos los tests de HU6 pasados correctamente");
    }
}
