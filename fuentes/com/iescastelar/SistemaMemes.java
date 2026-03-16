package com.iescastelar;

import java.util.Scanner;

public class SistemaMemes {

    static class Realidad {
        int    id;
        String textoCorrecto;
        String fuenteCorrecta;
        String textoFalso;
        String fuenteFalsa;

        Realidad(int id, String textoCorrecto, String fuenteCorrecta,
                         String textoFalso,    String fuenteFalsa) {
            this.id             = id;
            this.textoCorrecto  = textoCorrecto;
            this.fuenteCorrecta = fuenteCorrecta;
            this.textoFalso     = textoFalso;
            this.fuenteFalsa    = fuenteFalsa;
        }
    }

    // ── HU6: Comprueba la respuesta del usuario ───────────────────────────
    static int comprobarRespuesta(Scanner scanner, int opcionCorrecta,
                                   Realidad realidad, int puntuacion) {

        // Lee la respuesta del usuario y valida que sea 1, 2 o 3
        int respuesta = -1;
        while (respuesta < 1 || respuesta > 3) {
            try {
                respuesta = Integer.parseInt(scanner.nextLine().trim());
                if (respuesta < 1 || respuesta > 3)
                    System.out.print("Escribe 1, 2 o 3: ");
            } catch (NumberFormatException e) {
                System.out.print("Escribe 1, 2 o 3: ");
            }
        }

        // Comprueba si ha acertado
        if (respuesta == opcionCorrecta) {
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