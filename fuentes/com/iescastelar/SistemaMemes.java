package com.iescastelar;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * HU5 - Muestra un meme al azar y una lista numerada de realidades.
 * @author Adrián Tena Gallardo
 */
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

    /**
     * Muestra un meme al azar sin repetir y dos opciones mezcladas.
     * @param memes mapa de memes cargados
     * @param realidades lista de realidades cargadas
     * @param memesUsados lista de IDs ya mostrados
     * @param puntuacion puntuacion actual
     * @return numero de opcion correcta (1 o 2)
     */
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

        return opciones.indexOf(realidad.textoCorrecto) + 1;
    }
}