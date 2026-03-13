import java.io.*;
import java.util.*;

public class Ranking {

    private static final String ARCHIVO = "mejores.txt";
    private static final int MAX_PUNTUACIONES = 3;

    /**
     * Comprueba si la puntuación entra en el top 3 y, si es así,
     * pide el nombre al usuario y actualiza mejores.txt.
     *
     * @param puntuacionActual Puntuación obtenida por el usuario al finalizar la partida.
     */
    public static void gestionarRanking(int puntuacionActual) {
        List<int[]> mejores = cargarMejores(); // Cada int[]: [0]=puntuación, ya ordenado desc

        // Comprobar si entra en el top 3
        boolean entraEnRanking = mejores.size() < MAX_PUNTUACIONES
                || puntuacionActual > mejores.get(mejores.size() - 1)[1];

        if (entraEnRanking) {
            System.out.println("\n¡Enhorabuena! Tu puntuación de " + puntuacionActual
                    + " está entre las tres mejores.");
            System.out.print("Introduce tu nombre: ");
            Scanner sc = new Scanner(System.in);
            String nombre = sc.nextLine().trim();

            // Añadir la nueva entrada
            mejores.add(new int[]{0, puntuacionActual, 0}); // usaremos un mapa mejor
            guardarMejores(mejores, nombre, puntuacionActual);
        } else {
            System.out.println("\nTu puntuación: " + puntuacionActual
                    + ". No has entrado en el top " + MAX_PUNTUACIONES + ".");
            mostrarRanking();
        }
    }

    /**
     * Carga las puntuaciones desde mejores.txt.
     * Formato de cada línea: "nombre;puntuacion"
     *
     * @return Lista de entradas como String[] {nombre, puntuacion}, ordenada de mayor a menor.
     */
    private static List<String[]> cargarEntradas() {
        List<String[]> entradas = new ArrayList<>();
        File archivo = new File(ARCHIVO);

        try {
            if (!archivo.exists()) {
                archivo.createNewFile();
                System.out.println("Archivo " + ARCHIVO + " creado.");
                return entradas;
            }
        } catch (IOException e) {
            System.err.println("Error al crear " + ARCHIVO + ": " + e.getMessage());
            return entradas;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (!linea.isEmpty()) {
                    String[] partes = linea.split(";");
                    if (partes.length == 2) {
                        entradas.add(partes);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer " + ARCHIVO + ": " + e.getMessage());
        }

        return entradas;
    }

    /**
     * Versión simplificada que sólo devuelve las puntuaciones (entero) para comparar.
     */
    private static List<int[]> cargarMejores() {
        List<String[]> entradas = cargarEntradas();
        List<int[]> puntuaciones = new ArrayList<>();
        for (String[] e : entradas) {
            try {
                puntuaciones.add(new int[]{Integer.parseInt(e[1].trim())});
            } catch (NumberFormatException ex) {
                // línea malformada, se ignora
            }
        }
        // Ordenar de mayor a menor
        puntuaciones.sort((a, b) -> b[0] - a[0]);
        return puntuaciones;
    }

    /**
     * Añade la nueva puntuación al ranking, ordena y guarda en mejores.txt.
     */
    private static void guardarMejores(List<int[]> ignorado, String nombre, int puntuacion) {
        // Recargar entradas completas (con nombres)
        List<String[]> entradas = cargarEntradas();

        // Añadir la nueva
        entradas.add(new String[]{nombre, String.valueOf(puntuacion)});

        // Ordenar de mayor a menor por puntuación
        entradas.sort((a, b) -> Integer.parseInt(b[1]) - Integer.parseInt(a[1]));

        // Quedarse sólo con el top 3
        if (entradas.size() > MAX_PUNTUACIONES) {
            entradas = entradas.subList(0, MAX_PUNTUACIONES);
        }

        // Escribir en el archivo
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO))) {
            for (String[] entrada : entradas) {
                bw.write(entrada[0] + ";" + entrada[1]);
                bw.newLine();
            }
            System.out.println("\nRanking guardado en " + ARCHIVO);
        } catch (IOException e) {
            System.err.println("Error al escribir en " + ARCHIVO + ": " + e.getMessage());
        }

        // Mostrar el ranking actualizado
        mostrarRanking(entradas);
    }

    /**
     * Muestra el ranking leyendo desde el archivo.
     */
    public static void mostrarRanking() {
        mostrarRanking(cargarEntradas());
    }

    private static void mostrarRanking(List<String[]> entradas) {
        System.out.println("\n=== TOP " + MAX_PUNTUACIONES + " MEJORES PUNTUACIONES ===");
        if (entradas.isEmpty()) {
            System.out.println("  (sin registros aún)");
        } else {
            for (int i = 0; i < entradas.size(); i++) {
                System.out.printf("  %d. %-20s %s%n",
                        i + 1, entradas.get(i)[0], entradas.get(i)[1]);
            }
        }
        System.out.println("================================\n");
    }

    // -------------------------------------------------------------------------
    // Método main de prueba
    // -------------------------------------------------------------------------
    public static void main(String[] args) {
        // Simula el final de una partida con puntuación 150
        int puntuacionFinal = 150;
        gestionarRanking(puntuacionFinal);
    }
}
