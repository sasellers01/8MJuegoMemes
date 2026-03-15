package com.iescastelar;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class SistemaMemes {

    // ── Clase Realidad (HU4) ──────────────────────────────────────────────────
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

    // ── HU5: Muestra meme al azar y lista numerada de realidades ─────────────
    static int mostrarMemeYRealidades(Map<Integer, String> memes,
                                      List<Realidad>       realidades,
                                      List<Integer>        memesUsados,
                                      int                  puntuacion) {

        // Filtra los memes que todavía no han salido
        List<Integer> disponibles = new ArrayList<>(memes.keySet());
        disponibles.removeAll(memesUsados);

        // Elige uno al azar y lo marca como usado
        Random random = new Random();
        int    memeId = disponibles.get(random.nextInt(disponibles.size()));
        memesUsados.add(memeId);

        // Busca la Realidad con el mismo id que el meme
        Realidad realidad = null;
        for (Realidad r : realidades) {
            if (r.id == memeId) { realidad = r; break; }
        }

        // Mezcla correcta y falsa en orden aleatorio
        List<String> opciones = new ArrayList<>();
        opciones.add(realidad.textoCorrecto);
        opciones.add(realidad.textoFalso);
        Collections.shuffle(opciones, random);

        // Muestra marcador, meme y opciones numeradas
        System.out.println("\n══════════════════════════════════════════");
        System.out.println("  Puntuacion actual: " + puntuacion);
        System.out.println("══════════════════════════════════════════");
        System.out.println("\nMEME: " + memes.get(memeId));
        System.out.println("\nQue afirmacion desmiente este meme?\n");
        for (int i = 0; i < opciones.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + opciones.get(i));
        }
        System.out.println();

        // Devuelve qué número de opción es la correcta (1 o 2) para HU6
        return opciones.indexOf(realidad.textoCorrecto) + 1;
    }

    // ── Helpers de parseo ─────────────────────────────────────────────────────
    static int extraerEntero(String texto) {
        String resto = texto.substring(texto.indexOf(':') + 1).trim();
        StringBuilder sb = new StringBuilder();
        for (char c : resto.toCharArray()) {
            if (Character.isDigit(c)) sb.append(c);
            else if (sb.length() > 0) break;
        }
        return sb.length() > 0 ? Integer.parseInt(sb.toString()) : -1;
    }

    static String extraerJsonAnidado(String texto, String objeto, String clave) {
        int objPos      = texto.indexOf("\"" + objeto + "\"");
        if (objPos == -1) return null;
        int llaveAbre   = texto.indexOf('{', objPos);
        int llaveCierra = texto.indexOf('}', llaveAbre);
        if (llaveAbre == -1 || llaveCierra == -1) return null;
        return extraerJson(texto.substring(llaveAbre, llaveCierra + 1), clave);
    }

    static String extraerJson(String texto, String clave) {
        int i = texto.indexOf("\"" + clave + "\"");
        if (i == -1) return null;
        int inicio = texto.indexOf('"', texto.indexOf(':', i) + 1) + 1;
        int fin    = texto.indexOf('"', inicio);
        return (inicio > 0 && fin > inicio) ? texto.substring(inicio, fin) : null;
    }

    static Map<Integer, String> leerMemes(String ruta) throws IOException {
        Map<Integer, String> memes = new LinkedHashMap<>();
        for (String linea : Files.readAllLines(Path.of(ruta))) {
            linea = linea.trim();
            if (linea.isBlank() || linea.startsWith("#")) continue;
            String[] partes = linea.split("\\|", 2);
            if (partes.length != 2) continue;
            memes.put(Integer.parseInt(partes[0].trim()), partes[1].trim());
        }
        return memes;
    }

    static List<Realidad> leerRealidades(String ruta) throws IOException {
        List<Realidad> lista     = new ArrayList<>();
        String         contenido = Files.readString(Path.of(ruta));
        for (String bloque : contenido.split("\\{\\s*\"id\"")) {
            if (bloque.isBlank() || bloque.trim().startsWith("[")) continue;
            int    id             = extraerEntero(bloque);
            String textoCorrecto  = extraerJsonAnidado(bloque, "correcta", "realidad");
            String fuenteCorrecta = extraerJsonAnidado(bloque, "correcta", "fuente");
            String textoFalso     = extraerJsonAnidado(bloque, "falsa",    "realidad");
            String fuenteFalsa    = extraerJsonAnidado(bloque, "falsa",    "fuente");
            if (textoCorrecto != null && textoFalso != null)
                lista.add(new Realidad(id, textoCorrecto, fuenteCorrecta,
                                           textoFalso,    fuenteFalsa));
        }
        return lista;
    }

    // ── Main: prueba de HU5 ───────────────────────────────────────────────────
    public static void main(String[] args) throws Exception {
        Map<Integer, String> memes      = leerMemes("datos/memes.txt");
        List<Realidad>       realidades = leerRealidades("datos/realidades.json");
        List<Integer>        usados     = new ArrayList<>();

        // Prueba mostrando 3 memes consecutivos sin repetición
        for (int i = 0; i < 3; i++) {
            mostrarMemeYRealidades(memes, realidades, usados, 0);
        }
    }
}
