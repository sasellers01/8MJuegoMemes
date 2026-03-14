package com.iescastelar;
import java.util.*;
import java.io.*;
import java.nio.file.*;

/**
 * Archivo independiente del juego de memes.
 *
 * No depende de ninguna otra clase del proyecto.
 * Incluye toda la lógica necesaria:
 *   - Lectura de memes.txt, realidades.json y soluciones.xml
 *   - Bucle de 5 memes con respuesta del usuario
 *   - Marcado correcto (✓) / incorrecto (✗)
 *   - Ranking top 3 guardado en mejores.txt
 *
 * Rutas de los ficheros de datos (ajusta si es necesario):
 *   datos/memes.txt
 *   datos/realidades.json
 *   datos/soluciones.xml
 *   mejores.txt
 */
public class JuegoMemes {

    // ── Constantes ────────────────────────────────────────────────────────────
    private static final String FICHERO_MEMES      = "datos/memes.txt";
    private static final String FICHERO_REALIDADES = "datos/realidades.json";
    private static final String FICHERO_SOLUCIONES = "datos/soluciones.xml";
    private static final String FICHERO_RANKING    = "resultados/mejores.txt";
    private static final int    TOTAL_MEMES        = 5;
    private static final int    MAX_RANKING        = 3;

    // ── Clases internas de modelo ─────────────────────────────────────────────

    static class Meme {
        int    id;
        String texto;

        Meme(int id, String texto) {
            this.id    = id;
            this.texto = texto;
        }

        @Override
        public String toString() {
            return "[" + id + "] " + texto;
        }
    }

    static class Realidad {
        int    id;
        String texto;
        String fuente;

        Realidad(int id, String texto, String fuente) {
            this.id     = id;
            this.texto  = texto;
            this.fuente = fuente;
        }

        @Override
        public String toString() {
            return texto + "\n     Fuente: " + fuente;
        }
    }

    // ── Carga de datos ────────────────────────────────────────────────────────

    /** Lee memes.txt → Map<id, Meme> */
    static Map<Integer, Meme> cargarMemes() throws IOException {
        Map<Integer, Meme> mapa = new LinkedHashMap<>();
        for (String linea : Files.readAllLines(Path.of(FICHERO_MEMES))) {
            linea = linea.trim();
            if (linea.isEmpty() || linea.startsWith("#")) continue;
            String[] partes = linea.split("\\|", 2);
            if (partes.length < 2) continue;
            int    id    = Integer.parseInt(partes[0].trim());
            String texto = partes[1].trim();
            mapa.put(id, new Meme(id, texto));
        }
        return mapa;
    }

    /** Lee realidades.json → Map<id, Realidad> (parseo manual, sin dependencias) */
    static Map<Integer, Realidad> cargarRealidades() throws IOException {
        Map<Integer, Realidad> mapa = new LinkedHashMap<>();
        String contenido = Files.readString(Path.of(FICHERO_REALIDADES));

        // Dividir por objetos JSON principales (cada elemento del array)
        String[] objetos = contenido.split("\\{");
        int idActual = -1;

        for (String bloque : objetos) {
            // Intentar extraer "id" del nivel raíz
            String idStr = extraerJsonValor(bloque, "id");
            if (idStr != null) {
                try { idActual = Integer.parseInt(idStr.trim()); } catch (NumberFormatException ignored) {}
            }
            // Extraer campos dentro de "correcta"
            String texto  = extraerJsonValor(bloque, "realidad");
            String fuente = extraerJsonValor(bloque, "fuente");
            if (idActual != -1 && texto != null && fuente != null) {
                mapa.put(idActual, new Realidad(idActual, texto, fuente));
                idActual = -1;
            }
        }
        return mapa;
    }

    /** Lee soluciones.xml → Map<memeId, realidadId> (parseo manual, sin dependencias) */
    static Map<Integer, Integer> cargarSoluciones() throws IOException {
        Map<Integer, Integer> mapa = new LinkedHashMap<>();
        String contenido = Files.readString(Path.of(FICHERO_SOLUCIONES));
        for (String bloque : contenido.split("<solucion>")) {
            String memeStr     = extraerXml(bloque, "memeId");
            String realidadStr = extraerXml(bloque, "realidadId");
            if (memeStr != null && realidadStr != null) {
                try {
                    mapa.put(Integer.parseInt(memeStr.trim()),
                             Integer.parseInt(realidadStr.trim()));
                } catch (NumberFormatException ignored) {}
            }
        }
        return mapa;
    }

    // ── Helpers de parseo ─────────────────────────────────────────────────────

    static String extraerJsonValor(String texto, String clave) {
        int i = texto.indexOf("\"" + clave + "\"");
        if (i == -1) return null;
        int dospuntos = texto.indexOf(":", i + clave.length() + 2);
        if (dospuntos == -1) return null;
        // Valor numérico
        int comilla = texto.indexOf("\"", dospuntos + 1);
        int comaOLlave = texto.indexOf(",", dospuntos + 1);
        int corchete   = texto.indexOf("}", dospuntos + 1);
        int finNum     = (comaOLlave != -1 && corchete != -1) ? Math.min(comaOLlave, corchete)
                        : (comaOLlave != -1 ? comaOLlave : corchete);
        if (comilla == -1 || (finNum != -1 && finNum < comilla)) {
            // Es numérico
            if (finNum == -1) return null;
            return texto.substring(dospuntos + 1, finNum).trim();
        }
        // Es string
        int inicio = comilla + 1;
        int fin    = texto.indexOf("\"", inicio);
        return (fin > inicio) ? texto.substring(inicio, fin) : null;
    }

    static String extraerXml(String texto, String etiqueta) {
        int inicio = texto.indexOf("<" + etiqueta + ">");
        int fin    = texto.indexOf("</" + etiqueta + ">");
        if (inicio == -1 || fin == -1) return null;
        return texto.substring(inicio + etiqueta.length() + 2, fin).trim();
    }

    // ── Ranking ───────────────────────────────────────────────────────────────

    static List<String[]> cargarRanking() {
        List<String[]> entradas = new ArrayList<>();
        File archivo = new File(FICHERO_RANKING);
        if (!archivo.exists()) return entradas;
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (!linea.isEmpty()) {
                    String[] partes = linea.split(";");
                    if (partes.length == 2) entradas.add(partes);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer ranking: " + e.getMessage());
        }
        return entradas;
    }

    static void mostrarRanking(List<String[]> entradas) {
        System.out.println("\n══ TOP " + MAX_RANKING + " MEJORES PUNTUACIONES ══");
        if (entradas.isEmpty()) {
            System.out.println("  (sin registros aún)");
        } else {
            for (int i = 0; i < entradas.size(); i++) {
                System.out.printf("  %d. %-20s %s%n", i + 1, entradas.get(i)[0], entradas.get(i)[1]);
            }
        }
        System.out.println("═════════════════════════════\n");
    }

    static void gestionarRanking(int puntuacion, Scanner sc) {
        List<String[]> entradas = cargarRanking();

        boolean entraEnRanking = entradas.size() < MAX_RANKING
                || puntuacion > Integer.parseInt(entradas.get(entradas.size() - 1)[1].trim());

        if (entraEnRanking) {
            System.out.println("¡Enhorabuena! Tu puntuación de " + puntuacion + " entra en el top " + MAX_RANKING + ".");
            System.out.print("Introduce tu nombre: ");
            String nombre = sc.nextLine().trim();

            entradas.add(new String[]{nombre, String.valueOf(puntuacion)});
            entradas.sort((a, b) -> Integer.parseInt(b[1]) - Integer.parseInt(a[1]));
            if (entradas.size() > MAX_RANKING) entradas = entradas.subList(0, MAX_RANKING);

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(FICHERO_RANKING))) {
                for (String[] e : entradas) {
                    bw.write(e[0] + ";" + e[1]);
                    bw.newLine();
                }
            } catch (IOException e) {
                System.err.println("Error al guardar ranking: " + e.getMessage());
            }
        } else {
            System.out.println("Tu puntuación: " + puntuacion + ". No has entrado en el top " + MAX_RANKING + ".");
        }

        mostrarRanking(entradas);
    }

    // ── Main ──────────────────────────────────────────────────────────────────

    public static void main(String[] args) {

        System.out.println("═══════════════════════════════════════");
        System.out.println("        JUEGO DE MEMES VS REALIDAD     ");
        System.out.println("═══════════════════════════════════════\n");

        // Crear directorio resultados si no existe
        try {
            Files.createDirectories(Path.of("resultados"));
        } catch (IOException e) {
            System.out.println("No se pudo crear el directorio resultados: " + e.getMessage());
        }

        // Cargar datos
        Map<Integer, Meme>     memes;
        Map<Integer, Realidad> realidades;
        Map<Integer, Integer>  soluciones;

        try {
            memes      = cargarMemes();
            realidades = cargarRealidades();
            soluciones = cargarSoluciones();
        } catch (IOException e) {
            System.out.println("Error cargando datos: " + e.getMessage());
            return;
        }

        if (memes.isEmpty() || realidades.isEmpty() || soluciones.isEmpty()) {
            System.out.println("No se pudieron cargar los datos correctamente.");
            return;
        }

        System.out.println("Datos cargados: " + memes.size() + " memes, "
                + realidades.size() + " realidades.\n");

        List<Integer> ids    = new ArrayList<>(memes.keySet());
        Random        random = new Random();
        Scanner       sc     = new Scanner(System.in);
        int           puntos = 0;
        List<Integer> memesMostrados = new ArrayList<>();  // Control de repetición

        // ── Bucle de 5 memes con opciones numeradas ──────────────────────────
        for (int ronda = 1; ronda <= TOTAL_MEMES; ronda++) {

            System.out.println("──────────────────────────────────────");
            System.out.println("  MEME " + ronda + " de " + TOTAL_MEMES);
            System.out.println("──────────────────────────────────────");

            // Seleccionar meme aleatorio sin repetir
            int memeId;
            do {
                memeId = ids.get(random.nextInt(ids.size()));
            } while (memesMostrados.contains(memeId) && memesMostrados.size() < ids.size());
            memesMostrados.add(memeId);

            Meme   meme      = memes.get(memeId);
            int    realId    = soluciones.getOrDefault(memeId, -1);
            Realidad realidad = realidades.get(realId);

            System.out.println("\n  » " + meme.texto + "\n");

            // Crear lista de opciones (realidad correcta + 2 falsas de otros memes)
            List<Realidad> opciones = new ArrayList<>();
            opciones.add(realidad); // opción correcta

            // Obtener IDs de otras realidades (excluyendo la correcta)
            List<Integer> otrosIds = new ArrayList<>(realidades.keySet());
            otrosIds.remove((Integer) realId);
            Collections.shuffle(otrosIds);

            // Añadir hasta 2 opciones falsas
            for (int i = 0; i < Math.min(2, otrosIds.size()); i++) {
                opciones.add(realidades.get(otrosIds.get(i)));
            }

            // Barajar opciones para que el orden no sea siempre el mismo
            Collections.shuffle(opciones);

            // Mostrar opciones numeradas
            System.out.println("  Elige la realidad que desmiente el meme:");
            for (int i = 0; i < opciones.size(); i++) {
                System.out.println("    " + (i + 1) + ". " + opciones.get(i).texto);
            }
            System.out.print("\n  Introduce el número (1-" + opciones.size() + "): ");

            int eleccion;
            try {
                eleccion = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                eleccion = -1;
            }

            // Verificar si la opción elegida es la correcta
            boolean acierto = eleccion >= 1 && eleccion <= opciones.size()
                              && opciones.get(eleccion - 1).id == realidad.id;

            if (acierto) {
                System.out.println("\n  ✓  ¡CORRECTO!\n");
                puntos++;
            } else {
                System.out.println("\n  ✗  INCORRECTO");
                // Mostrar la realidad correcta
                System.out.println("  La realidad correcta era:");
                System.out.println("  → " + realidad + "\n");
            }

            // Mostrar marcador parcial
            System.out.println("  Marcador: " + puntos + "/" + ronda + "\n");
        }

        // ── Resultado final ───────────────────────────────────────────────
        System.out.println("═══════════════════════════════════════");
        System.out.println("  RESULTADO: " + puntos + " / " + TOTAL_MEMES + " correctos");
        System.out.println("═══════════════════════════════════════\n");

        gestionarRanking(puntos, sc);

        sc.close();
        System.out.println("=== FIN DEL PROGRAMA ===");
    }
}