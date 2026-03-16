package com.iescastelar;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class SistemaMemes {

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

    // ── HU5: Muestra meme al azar y lista numerada de realidades ─────────────
    static Integer mostrarMemeYRealidades(Map<Integer, String> memes,
                                          List<Realidad> realidades,
                                          List<Integer> memesUsados,
                                          Integer puntuacion) {

        List<Integer> disponibles = new ArrayList<>(memes.keySet());
        disponibles.removeAll(memesUsados);

        Random random = new Random();
        Integer memeId = disponibles.get(random.nextInt(disponibles.size()));
        memesUsados.add(memeId);

        Realidad realidad = null;
        for (Realidad r : realidades) {
            if (r.id.equals(memeId)) { realidad = r; break; }
        }

        List<String> opciones = new ArrayList<>();
        opciones.add(realidad.textoCorrecto);
        opciones.add(realidad.textoFalso);
        Collections.shuffle(opciones, random);

        System.out.println("\n══════════════════════════════════════════");
        System.out.println("  Puntuacion actual: " + puntuacion);
        System.out.println("══════════════════════════════════════════");
        System.out.println("\nMEME: " + memes.get(memeId));
        System.out.println("\nQue afirmacion desmiente este meme?\n");
        for (int i = 0; i < opciones.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + opciones.get(i));
        }
        System.out.println();

        return opciones.indexOf(realidad.textoCorrecto) + 1;
    }

    // ── HU6: Comprueba la respuesta del usuario ───────────────────────────────
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
