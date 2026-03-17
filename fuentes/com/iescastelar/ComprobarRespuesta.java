package com.iescastelar;

import java.util.Scanner;

/**
 * HU6 - Comprueba la respuesta del usuario.
 * @author Adrián Tena Gallardo
 */
public class ComprobarRespuesta {

    static class Realidad {
        Integer id;
        String textoCorrecto;
        String fuenteCorrecta;
        String textoFalso;
        String fuenteFalsa;

        Realidad(Integer id, String textoCorrecto, String fuenteCorrecta,
                 String textoFalso, String fuenteFalsa) {
            this.id = id;
            this.textoCorrecto = textoCorrecto;
            this.fuenteCorrecta = fuenteCorrecta;
            this.textoFalso = textoFalso;
            this.fuenteFalsa = fuenteFalsa;
        }
    }

    /**
     * Lee la respuesta del usuario y comprueba si es correcta.
     * @param scanner escaner de entrada
     * @param opcionCorrecta numero de opcion correcta (1, 2 o 3)
     * @param realidad objeto con la realidad correcta
     * @param puntuacion puntuacion actual
     * @return nueva puntuacion tras la respuesta
     */
    static Integer comprobarRespuesta(Scanner scanner, Integer opcionCorrecta,
                                      Realidad realidad, Integer puntuacion) {

        Integer respuesta = -1;
        while (respuesta < 1 || respuesta > 3) {
            try {
                respuesta = Integer.parseInt(scanner.nextLine().trim());
                if (respuesta < 1 || respuesta > 3)
                    System.out.print("Escribe 1, 2 o 3: ");
            } catch (NumberFormatException e) {
                System.out.print("Escribe 1, 2 o 3: ");
            }
        }

        if (respuesta.equals(opcionCorrecta)) {
            System.out.println("\n  ✓  ¡CORRECTO!");
            puntuacion++;
        } else {
            System.out.println("\n  ✗  INCORRECTO");
            System.out.println("  La realidad correcta era:");
            System.out.println("  → " + realidad.textoCorrecto);
            System.out.println("     Fuente: " + realidad.fuenteCorrecta);
        }

        return puntuacion;
    }
}
