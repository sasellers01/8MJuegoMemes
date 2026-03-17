package com.iescastelar;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.lang.reflect.*;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la clase Ranking.
 *
 * Dependencias necesarias en pom.xml (Maven):
 *
 *   <dependency>
 *       <groupId>org.junit.jupiter</groupId>
 *       <artifactId>junit-jupiter</artifactId>
 *       <version>5.10.2</version>
 *       <scope>test</scope>
 *   </dependency>
 *
 * O en build.gradle (Gradle):
 *   testImplementation 'org.junit.jupiter:junit-jupiter:5.10.2'
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RankingTest {

    // -----------------------------------------------------------------------
    // Constantes y campos auxiliares
    // -----------------------------------------------------------------------

    private static final String ARCHIVO = "mejores.txt";
    private static final int MAX = 3;

    /** Ruta original del archivo antes de redirigirlo. */
    private Path archivoOriginal;

    /** Directorio temporal que JUnit crea y borra automáticamente. */
    @TempDir
    Path tempDir;

    // -----------------------------------------------------------------------
    // Utilidades de reflexión para acceder a métodos privados
    // -----------------------------------------------------------------------

    /** Invoca el método privado cargarEntradas() de Ranking. */
    @SuppressWarnings("unchecked")
    private List<String[]> cargarEntradas() throws Exception {
        Method m = Ranking.class.getDeclaredMethod("cargarEntradas");
        m.setAccessible(true);
        return (List<String[]>) m.invoke(null);
    }

    /** Invoca el método privado cargarMejores() de Ranking. */
    @SuppressWarnings("unchecked")
    private List<int[]> cargarMejores() throws Exception {
        Method m = Ranking.class.getDeclaredMethod("cargarMejores");
        m.setAccessible(true);
        return (List<int[]>) m.invoke(null);
    }

    // -----------------------------------------------------------------------
    // Configuración: redirigir el campo estático ARCHIVO al directorio temporal
    // -----------------------------------------------------------------------

    @BeforeEach
    void setUp() throws Exception {
        // Cambia el campo ARCHIVO para apuntar al directorio temporal
        Field f = Ranking.class.getDeclaredField("ARCHIVO");
        f.setAccessible(true);

        // Guardar la ruta original para restaurar después
        archivoOriginal = Path.of((String) f.get(null));

        // Apuntar al directorio temporal
        String nuevaRuta = tempDir.resolve("mejores.txt").toString();
        // El campo es final, necesitamos hacerlo modificable
        Field modifiers = Field.class.getDeclaredField("modifiers");
        modifiers.setAccessible(true);
        modifiers.setInt(f, f.getModifiers() & ~java.lang.reflect.Modifier.FINAL);
        f.set(null, nuevaRuta);
    }

    @AfterEach
    void tearDown() throws Exception {
        // Restaurar el campo ARCHIVO original
        Field f = Ranking.class.getDeclaredField("ARCHIVO");
        f.setAccessible(true);
        Field modifiers = Field.class.getDeclaredField("modifiers");
        modifiers.setAccessible(true);
        modifiers.setInt(f, f.getModifiers() & ~java.lang.reflect.Modifier.FINAL);
        f.set(null, archivoOriginal.toString());
    }

    // -----------------------------------------------------------------------
    // Helper: escribe líneas directamente en el archivo de ranking
    // -----------------------------------------------------------------------

    private void escribirArchivo(String... lineas) throws IOException {
        Field f;
        try {
            f = Ranking.class.getDeclaredField("ARCHIVO");
            f.setAccessible(true);
            String ruta = (String) f.get(null);
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
                for (String linea : lineas) {
                    bw.write(linea);
                    bw.newLine();
                }
            }
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    // -----------------------------------------------------------------------
    // TESTS: cargarEntradas()
    // -----------------------------------------------------------------------

    @Test
    @Order(1)
    @DisplayName("cargarEntradas() — archivo inexistente devuelve lista vacía y lo crea")
    void testCargarEntradas_archivoInexistente() throws Exception {
        List<String[]> resultado = cargarEntradas();
        assertTrue(resultado.isEmpty(), "Debe devolver lista vacía si no hay archivo");

        Field f = Ranking.class.getDeclaredField("ARCHIVO");
        f.setAccessible(true);
        assertTrue(new File((String) f.get(null)).exists(), "El archivo debe haberse creado");
    }

    @Test
    @Order(2)
    @DisplayName("cargarEntradas() — lee correctamente líneas con formato 'nombre;puntuacion'")
    void testCargarEntradas_conDatos() throws Exception {
        escribirArchivo("Alice;300", "Bob;200", "Carol;100");
        List<String[]> resultado = cargarEntradas();

        assertEquals(3, resultado.size());
        assertEquals("Alice", resultado.get(0)[0]);
        assertEquals("300",   resultado.get(0)[1]);
        assertEquals("Bob",   resultado.get(1)[0]);
        assertEquals("Carol", resultado.get(2)[0]);
    }

    @Test
    @Order(3)
    @DisplayName("cargarEntradas() — ignora líneas malformadas")
    void testCargarEntradas_lineasMalformadas() throws Exception {
        escribirArchivo("Alice;300", "linea_sin_punto_y_coma", "", "Bob;200");
        List<String[]> resultado = cargarEntradas();

        assertEquals(2, resultado.size(), "Solo deben cargarse 2 entradas válidas");
    }

    // -----------------------------------------------------------------------
    // TESTS: cargarMejores()
    // -----------------------------------------------------------------------

    @Test
    @Order(4)
    @DisplayName("cargarMejores() — devuelve puntuaciones ordenadas de mayor a menor")
    void testCargarMejores_ordenDesc() throws Exception {
        escribirArchivo("Alice;100", "Bob;300", "Carol;200");
        List<int[]> mejores = cargarMejores();

        assertEquals(3, mejores.size());
        assertEquals(300, mejores.get(0)[0]);
        assertEquals(200, mejores.get(1)[0]);
        assertEquals(100, mejores.get(2)[0]);
    }

    @Test
    @Order(5)
    @DisplayName("cargarMejores() — archivo vacío devuelve lista vacía")
    void testCargarMejores_archivoVacio() throws Exception {
        escribirArchivo(); // sin líneas
        List<int[]> mejores = cargarMejores();
        assertTrue(mejores.isEmpty());
    }

    // -----------------------------------------------------------------------
    // TESTS: gestionarRanking() — simulando entrada por teclado con System.in
    // -----------------------------------------------------------------------

    /**
     * Redirige System.in y System.out para poder simular la entrada del usuario
     * y capturar la salida de consola.
     */
    private String ejecutarGestionarRanking(int puntuacion, String inputUsuario) throws Exception {
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;
        try {
            // Simular entrada de usuario
            System.setIn(new ByteArrayInputStream(inputUsuario.getBytes()));
            // Capturar salida
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(baos));

            Ranking.gestionarRanking(puntuacion);

            return baos.toString();
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }

    @Test
    @Order(6)
    @DisplayName("gestionarRanking() — primera puntuación siempre entra en ranking vacío")
    void testGestionarRanking_primeraPuntuacion() throws Exception {
        String salida = ejecutarGestionarRanking(150, "Jugador1\n");

        assertTrue(salida.contains("Enhorabuena"), "Debe felicitar al jugador");
        // Verificar que se guardó
        List<String[]> entradas = cargarEntradas();
        assertEquals(1, entradas.size());
        assertEquals("Jugador1", entradas.get(0)[0]);
        assertEquals("150", entradas.get(0)[1]);
    }

    @Test
    @Order(7)
    @DisplayName("gestionarRanking() — puntuación que entra en top 3 se guarda correctamente")
    void testGestionarRanking_entraEnTop3() throws Exception {
        escribirArchivo("Alice;300", "Bob;200");

        String salida = ejecutarGestionarRanking(250, "Carol\n");

        assertTrue(salida.contains("Enhorabuena"));
        List<String[]> entradas = cargarEntradas();
        // Debe haber 3 entradas ordenadas
        assertEquals(3, entradas.size());
        assertEquals("300", entradas.get(0)[1]);
        assertEquals("250", entradas.get(1)[1]);
        assertEquals("200", entradas.get(2)[1]);
    }

    @Test
    @Order(8)
    @DisplayName("gestionarRanking() — puntuación más baja que el top 3 NO entra")
    void testGestionarRanking_noEntraEnRanking() throws Exception {
        escribirArchivo("Alice;300", "Bob;200", "Carol;100");

        String salida = ejecutarGestionarRanking(50, "");

        assertFalse(salida.contains("Enhorabuena"), "No debe felicitar si no entra en el ranking");
        assertTrue(salida.contains("No has entrado") || salida.contains("TOP"),
                "Debe informar de que no ha entrado y/o mostrar el ranking");

        // El archivo NO debe haberse modificado
        List<String[]> entradas = cargarEntradas();
        assertEquals(3, entradas.size());
    }

    @Test
    @Order(9)
    @DisplayName("gestionarRanking() — al superar la mínima del top 3, la peor puntuación es eliminada")
    void testGestionarRanking_eliminaLaPeorPuntuacion() throws Exception {
        escribirArchivo("Alice;300", "Bob;200", "Carol;100");

        ejecutarGestionarRanking(150, "David\n");

        List<String[]> entradas = cargarEntradas();
        assertEquals(3, entradas.size(), "El top 3 no debe crecer más de 3 entradas");

        // Carol (100) debe haber sido desplazada
        boolean carolPresente = entradas.stream()
                .anyMatch(e -> "Carol".equals(e[0]) && "100".equals(e[1]));
        assertFalse(carolPresente, "Carol con 100 puntos debería haber sido eliminada del top 3");

        // David (150) debe estar
        boolean davidPresente = entradas.stream()
                .anyMatch(e -> "David".equals(e[0]) && "150".equals(e[1]));
        assertTrue(davidPresente, "David con 150 puntos debe estar en el top 3");
    }

    @Test
    @Order(10)
    @DisplayName("gestionarRanking() — empate en la puntuación mínima entra en el ranking")
    void testGestionarRanking_empateEnMinimo() throws Exception {
        escribirArchivo("Alice;300", "Bob;200");
        // Solo hay 2 entradas → cualquier puntuación entra
        String salida = ejecutarGestionarRanking(200, "Eve\n");
        assertTrue(salida.contains("Enhorabuena"));
    }

    // -----------------------------------------------------------------------
    // TESTS: mostrarRanking()
    // -----------------------------------------------------------------------

    @Test
    @Order(11)
    @DisplayName("mostrarRanking() — muestra encabezado aunque no haya registros")
    void testMostrarRanking_sinRegistros() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(baos));
        try {
            Ranking.mostrarRanking();
        } finally {
            System.setOut(originalOut);
        }
        String salida = baos.toString();
        assertTrue(salida.contains("TOP"), "Debe mostrar el encabezado TOP");
        assertTrue(salida.contains("sin registros") || salida.contains("(sin"),
                "Debe indicar que no hay registros");
    }

    @Test
    @Order(12)
    @DisplayName("mostrarRanking() — muestra correctamente los 3 primeros")
    void testMostrarRanking_conRegistros() throws Exception {
        escribirArchivo("Alice;300", "Bob;200", "Carol;100");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(baos));
        try {
            Ranking.mostrarRanking();
        } finally {
            System.setOut(originalOut);
        }
        String salida = baos.toString();
        assertTrue(salida.contains("Alice"), "Debe mostrar el nombre Alice");
        assertTrue(salida.contains("300"),   "Debe mostrar la puntuación 300");
        assertTrue(salida.contains("Bob"),   "Debe mostrar el nombre Bob");
        assertTrue(salida.contains("Carol"), "Debe mostrar el nombre Carol");
    }

    // -----------------------------------------------------------------------
    // TESTS: robustez ante datos corruptos
    // -----------------------------------------------------------------------

    @Test
    @Order(13)
    @DisplayName("cargarEntradas() — puntuación no numérica es ignorada en cargarMejores")
    void testCargarMejores_puntuacionNoNumerica() throws Exception {
        escribirArchivo("Alice;abc", "Bob;200");
        List<int[]> mejores = cargarMejores();
        // Solo "Bob;200" es válido
        assertEquals(1, mejores.size());
        assertEquals(200, mejores.get(0)[0]);
    }

    @Test
    @Order(14)
    @DisplayName("Integridad: el archivo nunca supera MAX_PUNTUACIONES tras varias partidas")
    void testIntegridad_archivoNuncaSuperaMax() throws Exception {
        String[] nombres = {"Ana", "Luis", "Marta", "Pedro", "Sara"};
        int[] puntos   = {500,   400,    300,     200,    100};

        for (int i = 0; i < nombres.length; i++) {
            ejecutarGestionarRanking(puntos[i], nombres[i] + "\n");
        }

        List<String[]> entradas = cargarEntradas();
        assertTrue(entradas.size() <= MAX,
                "El archivo no debe tener más de " + MAX + " entradas, tiene: " + entradas.size());
    }
}
