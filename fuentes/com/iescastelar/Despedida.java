package com.iescastelar;
import java.io.*;
import java.util.*;

public class Despedida {

    private static final String ARCHIVO = "mejores.txt";
    private static final int MAX_PUNTUACIONES = 3;

    /**
     * Muestra la lista de mejores puntuaciones y se despide del usuario.
     * Llamar al final de la partida, tras gestionar el ranking.
     */
    public static void mostrarRankingYDespedirse() {
        List<String[]> entradas = cargarEntradas();

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

        System.out.println("¡Gracias por jugar! Hasta la próxima. 👋");
    }

    
    private static List<String[]> cargarEntradas() {
        List<String[]> entradas = new ArrayList<>();
        File archivo = new File(ARCHIVO);

        if (!archivo.exists()) {
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

    // -------------------------------------------------------------------------
    // Método main de prueba
    // -------------------------------------------------------------------------
    public static void main(String[] args) {
        mostrarRankingYDespedirse();
    }
}
