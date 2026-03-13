import java.io.*;
import java.util.*;

/**
 * Muestra el ranking de mejores puntuaciones guardado en mejores.txt.
 * Uso: javac MostrarRanking.java && java MostrarRanking
 */
public class MostrarRanking {

    private static final String ARCHIVO       = "mejores.txt";
    private static final int    MAX_POSICIONES = 3;

    public static void main(String[] args) {

        System.out.println("═══════════════════════════════════════");
        System.out.println("    TOP " + MAX_POSICIONES + " MEJORES PUNTUACIONES      ");
        System.out.println("═══════════════════════════════════════");

        List<String[]> entradas = cargarRanking();

        if (entradas.isEmpty()) {
            System.out.println("  (sin registros aún)");
        } else {
            for (int i = 0; i < entradas.size(); i++) {
                System.out.printf("  %d.  %-20s %s puntos%n",
                        i + 1,
                        entradas.get(i)[0],
                        entradas.get(i)[1]);
            }
        }

        System.out.println("═══════════════════════════════════════");
        System.out.println("\nPulsa ENTER para salir...");

        try { System.in.read(); } catch (IOException ignored) {}
    }

    private static List<String[]> cargarRanking() {
        List<String[]> entradas = new ArrayList<>();
        File archivo = new File(ARCHIVO);

        if (!archivo.exists()) {
            System.out.println("  No se encontró el archivo " + ARCHIVO);
            return entradas;
        }

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
            System.err.println("Error al leer " + ARCHIVO + ": " + e.getMessage());
        }

        // Ordenar de mayor a menor por puntuación
        entradas.sort((a, b) -> Integer.parseInt(b[1].trim()) - Integer.parseInt(a[1].trim()));

        return entradas;
    }
}
