package com.iescastelar;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.lang.reflect.*;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la clase JuegoMemes.
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
class JuegoMemesTest {

    private static final int MAX_RANKING = 3;

    @TempDir
    Path tempDir;

    // -----------------------------------------------------------------------
    // Helpers: redirigir campos estáticos al directorio temporal
    // -----------------------------------------------------------------------

    private void setField(String fieldName, String valor) throws Exception {
        Field f = JuegoMemes.class.getDeclaredField(fieldName);
        f.setAccessible(true);
        Field mod = Field.class.getDeclaredField("modifiers");
        mod.setAccessible(true);
        mod.setInt(f, f.getModifiers() & ~Modifier.FINAL);
        f.set(null, valor);
    }

    private String getField(String fieldName) throws Exception {
        Field f = JuegoMemes.class.getDeclaredField(fieldName);
        f.setAccessible(true);
        return (String) f.get(null);
    }

    /** Escribe un archivo en el directorio temporal y redirige el campo estático indicado. */
    private Path crearArchivo(String fieldName, String nombreArchivo, String contenido) throws Exception {
        Path archivo = tempDir.resolve(nombreArchivo);
        Files.writeString(archivo, contenido);
        setField(fieldName, archivo.toString());
        return archivo;
    }

    /** Escribe directamente en el archivo de ranking. */
    private void escribirRanking(String... lineas) throws Exception {
        Path archivo = tempDir.resolve("mejores.txt");
        setField("FICHERO_RANKING", archivo.toString());
        Files.write(archivo, Arrays.asList(lineas));
    }

    // -----------------------------------------------------------------------
    // Datos de prueba reutilizables
    // -----------------------------------------------------------------------

    private static final String MEMES_VALIDOS =
            "1 | Los videojuegos causan violencia\n" +
            "2 | El azúcar hace hiperactivos a los niños\n" +
            "3 | Solo usamos el 10% del cerebro\n";

    private static final String REALIDADES_VALIDAS =
            "[\n" +
            "  { \"id\": 1, \"correcta\": { \"realidad\": \"Ningún estudio científico lo demuestra\", \"fuente\": \"APA 2020\" } },\n" +
            "  { \"id\": 2, \"correcta\": { \"realidad\": \"Es un mito sin base científica\", \"fuente\": \"NEJM 1995\" } },\n" +
            "  { \"id\": 3, \"correcta\": { \"realidad\": \"Usamos todo el cerebro en distintos momentos\", \"fuente\": \"Nature 2013\" } }\n" +
            "]";

    private static final String SOLUCIONES_VALIDAS =
            "<soluciones>\n" +
            "  <solucion><memeId>1</memeId><realidadId>1</realidadId></solucion>\n" +
            "  <solucion><memeId>2</memeId><realidadId>2</realidadId></solucion>\n" +
            "  <solucion><memeId>3</memeId><realidadId>3</realidadId></solucion>\n" +
            "</soluciones>";

    // -----------------------------------------------------------------------
    // TESTS: cargarMemes()
    // -----------------------------------------------------------------------

    @Test
    @Order(1)
    @DisplayName("cargarMemes() — lee correctamente el formato 'id | texto'")
    void testCargarMemes_lecturaNormal() throws Exception {
        crearArchivo("FICHERO_MEMES", "memes.txt", MEMES_VALIDOS);

        Map<Integer, JuegoMemes.Meme> memes = JuegoMemes.cargarMemes();

        assertEquals(3, memes.size());
        assertTrue(memes.containsKey(1));
        assertEquals("Los videojuegos causan violencia", memes.get(1).texto);
        assertEquals(2, memes.get(2).id);
    }

    @Test
    @Order(2)
    @DisplayName("cargarMemes() — ignora líneas vacías y comentarios (#)")
    void testCargarMemes_ignoraLineasInvalidas() throws Exception {
        String contenido =
                "# Esto es un comentario\n" +
                "\n" +
                "1 | Meme válido\n" +
                "linea_sin_separador\n" +
                "2 | Otro meme\n";
        crearArchivo("FICHERO_MEMES", "memes.txt", contenido);

        Map<Integer, JuegoMemes.Meme> memes = JuegoMemes.cargarMemes();

        assertEquals(2, memes.size(), "Solo deben cargarse 2 memes válidos");
    }

    @Test
    @Order(3)
    @DisplayName("cargarMemes() — archivo vacío devuelve mapa vacío")
    void testCargarMemes_archivoVacio() throws Exception {
        crearArchivo("FICHERO_MEMES", "memes.txt", "");
        Map<Integer, JuegoMemes.Meme> memes = JuegoMemes.cargarMemes();
        assertTrue(memes.isEmpty());
    }

    @Test
    @Order(4)
    @DisplayName("cargarMemes() — lanza IOException si el archivo no existe")
    void testCargarMemes_archivoInexistente() throws Exception {
        setField("FICHERO_MEMES", tempDir.resolve("no_existe.txt").toString());
        assertThrows(IOException.class, JuegoMemes::cargarMemes);
    }

    // -----------------------------------------------------------------------
    // TESTS: cargarRealidades()
    // -----------------------------------------------------------------------

    @Test
    @Order(5)
    @DisplayName("cargarRealidades() — parsea correctamente el JSON manual")
    void testCargarRealidades_lecturaNormal() throws Exception {
        crearArchivo("FICHERO_REALIDADES", "realidades.json", REALIDADES_VALIDAS);

        Map<Integer, JuegoMemes.Realidad> realidades = JuegoMemes.cargarRealidades();

        assertEquals(3, realidades.size());
        assertTrue(realidades.containsKey(1));
        assertEquals("Ningún estudio científico lo demuestra", realidades.get(1).texto);
        assertEquals("APA 2020", realidades.get(1).fuente);
    }

    @Test
    @Order(6)
    @DisplayName("cargarRealidades() — archivo vacío devuelve mapa vacío")
    void testCargarRealidades_archivoVacio() throws Exception {
        crearArchivo("FICHERO_REALIDADES", "realidades.json", "[]");
        Map<Integer, JuegoMemes.Realidad> realidades = JuegoMemes.cargarRealidades();
        assertTrue(realidades.isEmpty());
    }

    @Test
    @Order(7)
    @DisplayName("cargarRealidades() — lanza IOException si el archivo no existe")
    void testCargarRealidades_archivoInexistente() throws Exception {
        setField("FICHERO_REALIDADES", tempDir.resolve("no_existe.json").toString());
        assertThrows(IOException.class, JuegoMemes::cargarRealidades);
    }

    // -----------------------------------------------------------------------
    // TESTS: cargarSoluciones()
    // -----------------------------------------------------------------------

    @Test
    @Order(8)
    @DisplayName("cargarSoluciones() — parsea correctamente el XML manual")
    void testCargarSoluciones_lecturaNormal() throws Exception {
        crearArchivo("FICHERO_SOLUCIONES", "soluciones.xml", SOLUCIONES_VALIDAS);

        Map<Integer, Integer> soluciones = JuegoMemes.cargarSoluciones();

        assertEquals(3, soluciones.size());
        assertEquals(1, soluciones.get(1));
        assertEquals(2, soluciones.get(2));
        assertEquals(3, soluciones.get(3));
    }

    @Test
    @Order(9)
    @DisplayName("cargarSoluciones() — XML sin entradas devuelve mapa vacío")
    void testCargarSoluciones_archivoVacio() throws Exception {
        crearArchivo("FICHERO_SOLUCIONES", "soluciones.xml", "<soluciones></soluciones>");
        Map<Integer, Integer> soluciones = JuegoMemes.cargarSoluciones();
        assertTrue(soluciones.isEmpty());
    }

    @Test
    @Order(10)
    @DisplayName("cargarSoluciones() — lanza IOException si el archivo no existe")
    void testCargarSoluciones_archivoInexistente() throws Exception {
        setField("FICHERO_SOLUCIONES", tempDir.resolve("no_existe.xml").toString());
        assertThrows(IOException.class, JuegoMemes::cargarSoluciones);
    }

    // -----------------------------------------------------------------------
    // TESTS: extraerJsonValor()
    // -----------------------------------------------------------------------

    @Test
    @Order(11)
    @DisplayName("extraerJsonValor() — extrae valor string correctamente")
    void testExtraerJsonValor_string() {
        String json = "\"realidad\": \"Es un mito\", \"fuente\": \"APA\"";
        assertEquals("Es un mito", JuegoMemes.extraerJsonValor(json, "realidad"));
        assertEquals("APA",        JuegoMemes.extraerJsonValor(json, "fuente"));
    }

    @Test
    @Order(12)
    @DisplayName("extraerJsonValor() — extrae valor numérico correctamente")
    void testExtraerJsonValor_numerico() {
        String json = "\"id\": 42, \"otro\": \"valor\"";
        assertEquals("42", JuegoMemes.extraerJsonValor(json, "id").trim());
    }

    @Test
    @Order(13)
    @DisplayName("extraerJsonValor() — devuelve null si la clave no existe")
    void testExtraerJsonValor_claveInexistente() {
        String json = "\"nombre\": \"test\"";
        assertNull(JuegoMemes.extraerJsonValor(json, "inexistente"));
    }

    // -----------------------------------------------------------------------
    // TESTS: extraerXml()
    // -----------------------------------------------------------------------

    @Test
    @Order(14)
    @DisplayName("extraerXml() — extrae contenido entre etiquetas correctamente")
    void testExtraerXml_normal() {
        String xml = "<memeId>5</memeId><realidadId>3</realidadId>";
        assertEquals("5", JuegoMemes.extraerXml(xml, "memeId"));
        assertEquals("3", JuegoMemes.extraerXml(xml, "realidadId"));
    }

    @Test
    @Order(15)
    @DisplayName("extraerXml() — devuelve null si la etiqueta no existe")
    void testExtraerXml_etiquetaInexistente() {
        String xml = "<memeId>5</memeId>";
        assertNull(JuegoMemes.extraerXml(xml, "noExiste"));
    }

    @Test
    @Order(16)
    @DisplayName("extraerXml() — devuelve null si solo existe la etiqueta de apertura")
    void testExtraerXml_etiquetaIncompleta() {
        String xml = "<memeId>5";
        assertNull(JuegoMemes.extraerXml(xml, "memeId"));
    }

    // -----------------------------------------------------------------------
    // TESTS: cargarRanking()
    // -----------------------------------------------------------------------

    @Test
    @Order(17)
    @DisplayName("cargarRanking() — archivo inexistente devuelve lista vacía")
    void testCargarRanking_archivoInexistente() throws Exception {
        setField("FICHERO_RANKING", tempDir.resolve("no_existe.txt").toString());
        List<String[]> ranking = JuegoMemes.cargarRanking();
        assertTrue(ranking.isEmpty());
    }

    @Test
    @Order(18)
    @DisplayName("cargarRanking() — lee entradas con formato 'nombre;puntuacion'")
    void testCargarRanking_conDatos() throws Exception {
        escribirRanking("Alice;5", "Bob;3", "Carol;1");
        List<String[]> ranking = JuegoMemes.cargarRanking();

        assertEquals(3, ranking.size());
        assertEquals("Alice", ranking.get(0)[0]);
        assertEquals("5",     ranking.get(0)[1]);
    }

    @Test
    @Order(19)
    @DisplayName("cargarRanking() — ignora líneas malformadas")
    void testCargarRanking_lineasMalformadas() throws Exception {
        escribirRanking("Alice;5", "linea_invalida", "", "Bob;3");
        List<String[]> ranking = JuegoMemes.cargarRanking();
        assertEquals(2, ranking.size());
    }

    // -----------------------------------------------------------------------
    // TESTS: mostrarRanking()
    // -----------------------------------------------------------------------

    @Test
    @Order(20)
    @DisplayName("mostrarRanking() — lista vacía muestra mensaje '(sin registros aún)'")
    void testMostrarRanking_vacio() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(baos));
        try {
            JuegoMemes.mostrarRanking(new ArrayList<>());
        } finally {
            System.setOut(original);
        }
        assertTrue(baos.toString().contains("sin registros"));
    }

    @Test
    @Order(21)
    @DisplayName("mostrarRanking() — muestra nombres y puntuaciones correctamente")
    void testMostrarRanking_conDatos() {
        List<String[]> entradas = List.of(
                new String[]{"Alice", "5"},
                new String[]{"Bob",   "3"}
        );
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(baos));
        try {
            JuegoMemes.mostrarRanking(entradas);
        } finally {
            System.setOut(original);
        }
        String salida = baos.toString();
        assertTrue(salida.contains("Alice"));
        assertTrue(salida.contains("5"));
        assertTrue(salida.contains("Bob"));
    }

    // -----------------------------------------------------------------------
    // TESTS: gestionarRanking()
    // -----------------------------------------------------------------------

    /** Ejecuta gestionarRanking simulando entrada del usuario. */
    private String ejecutarGestionarRanking(int puntuacion, String inputUsuario) throws Exception {
        InputStream originalIn  = System.in;
        PrintStream originalOut = System.out;
        try {
            System.setIn(new ByteArrayInputStream(inputUsuario.getBytes()));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(baos));
            Scanner sc = new Scanner(System.in);
            JuegoMemes.gestionarRanking(puntuacion, sc);
            return baos.toString();
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }

    @Test
    @Order(22)
    @DisplayName("gestionarRanking() — primera puntuación siempre entra en ranking vacío")
    void testGestionarRanking_primeraPuntuacion() throws Exception {
        setField("FICHERO_RANKING", tempDir.resolve("mejores.txt").toString());

        String salida = ejecutarGestionarRanking(4, "Jugador1\n");

        assertTrue(salida.contains("Enhorabuena"));
        List<String[]> ranking = JuegoMemes.cargarRanking();
        assertEquals(1, ranking.size());
        assertEquals("Jugador1", ranking.get(0)[0]);
        assertEquals("4",        ranking.get(0)[1]);
    }

    @Test
    @Order(23)
    @DisplayName("gestionarRanking() — puntuación mayor que la mínima entra en top 3")
    void testGestionarRanking_entraEnTop3() throws Exception {
        escribirRanking("Alice;5", "Bob;3");

        String salida = ejecutarGestionarRanking(4, "Carol\n");

        assertTrue(salida.contains("Enhorabuena"));
        List<String[]> ranking = JuegoMemes.cargarRanking();
        assertEquals(3, ranking.size());
        assertEquals("5", ranking.get(0)[1]);
        assertEquals("4", ranking.get(1)[1]);
        assertEquals("3", ranking.get(2)[1]);
    }

    @Test
    @Order(24)
    @DisplayName("gestionarRanking() — puntuación inferior al top 3 completo NO entra")
    void testGestionarRanking_noEntraEnRanking() throws Exception {
        escribirRanking("Alice;5", "Bob;4", "Carol;3");

        String salida = ejecutarGestionarRanking(1, "");

        assertFalse(salida.contains("Enhorabuena"));
        assertTrue(salida.contains("No has entrado") || salida.contains("TOP"));

        // El archivo no debe haberse modificado
        List<String[]> ranking = JuegoMemes.cargarRanking();
        assertEquals(3, ranking.size());
    }

    @Test
    @Order(25)
    @DisplayName("gestionarRanking() — al entrar en top 3 completo, la peor puntuación se elimina")
    void testGestionarRanking_eliminaLaPeorPuntuacion() throws Exception {
        escribirRanking("Alice;5", "Bob;4", "Carol;2");

        ejecutarGestionarRanking(3, "David\n");

        List<String[]> ranking = JuegoMemes.cargarRanking();
        assertEquals(3, ranking.size(), "El top 3 no debe superar 3 entradas");

        boolean carolPresente = ranking.stream()
                .anyMatch(e -> "Carol".equals(e[0]) && "2".equals(e[1]));
        assertFalse(carolPresente, "Carol con 2 puntos debería haber sido eliminada");

        boolean davidPresente = ranking.stream()
                .anyMatch(e -> "David".equals(e[0]) && "3".equals(e[1]));
        assertTrue(davidPresente, "David con 3 puntos debe estar en el ranking");
    }

    @Test
    @Order(26)
    @DisplayName("gestionarRanking() — el ranking queda ordenado de mayor a menor")
    void testGestionarRanking_ordenCorrecto() throws Exception {
        escribirRanking("Alice;3", "Bob;1");

        ejecutarGestionarRanking(2, "Carol\n");

        List<String[]> ranking = JuegoMemes.cargarRanking();
        assertEquals("3", ranking.get(0)[1]);
        assertEquals("2", ranking.get(1)[1]);
        assertEquals("1", ranking.get(2)[1]);
    }

    // -----------------------------------------------------------------------
    // TESTS: integración cargarMemes + cargarSoluciones + cargarRealidades
    // -----------------------------------------------------------------------

    @Test
    @Order(27)
    @DisplayName("Integración — los ids de soluciones coinciden con memes y realidades cargados")
    void testIntegracion_idsConsistentes() throws Exception {
        crearArchivo("FICHERO_MEMES",      "memes.txt",      MEMES_VALIDOS);
        crearArchivo("FICHERO_REALIDADES", "realidades.json", REALIDADES_VALIDAS);
        crearArchivo("FICHERO_SOLUCIONES", "soluciones.xml",  SOLUCIONES_VALIDAS);

        Map<Integer, JuegoMemes.Meme>     memes      = JuegoMemes.cargarMemes();
        Map<Integer, JuegoMemes.Realidad> realidades = JuegoMemes.cargarRealidades();
        Map<Integer, Integer>             soluciones = JuegoMemes.cargarSoluciones();

        for (Map.Entry<Integer, Integer> entrada : soluciones.entrySet()) {
            int memeId    = entrada.getKey();
            int realidadId = entrada.getValue();
            assertTrue(memes.containsKey(memeId),
                    "El memeId " + memeId + " de soluciones no existe en memes");
            assertTrue(realidades.containsKey(realidadId),
                    "El realidadId " + realidadId + " de soluciones no existe en realidades");
        }
    }

    @Test
    @Order(28)
    @DisplayName("Integridad ranking — nunca supera MAX_RANKING tras múltiples partidas")
    void testIntegridad_rankingNuncaSuperaMax() throws Exception {
        setField("FICHERO_RANKING", tempDir.resolve("mejores.txt").toString());

        String[] nombres = {"Ana", "Luis", "Marta", "Pedro", "Sara"};
        int[]    puntos  = {5,      4,      3,       2,       1};

        for (int i = 0; i < nombres.length; i++) {
            ejecutarGestionarRanking(puntos[i], nombres[i] + "\n");
        }

        List<String[]> ranking = JuegoMemes.cargarRanking();
        assertTrue(ranking.size() <= MAX_RANKING,
                "El ranking no debe superar " + MAX_RANKING + " entradas, tiene: " + ranking.size());
    }

    // -----------------------------------------------------------------------
    // TESTS: modelo Meme y Realidad
    // -----------------------------------------------------------------------

    @Test
    @Order(29)
    @DisplayName("Meme.toString() — devuelve '[id] texto'")
    void testMemeToString() {
        JuegoMemes.Meme meme = new JuegoMemes.Meme(1, "Los videojuegos causan violencia");
        assertEquals("[1] Los videojuegos causan violencia", meme.toString());
    }

    @Test
    @Order(30)
    @DisplayName("Realidad.toString() — contiene el texto y la fuente")
    void testRealidadToString() {
        JuegoMemes.Realidad r = new JuegoMemes.Realidad(1, "No hay evidencia", "APA 2020");
        String str = r.toString();
        assertTrue(str.contains("No hay evidencia"));
        assertTrue(str.contains("APA 2020"));
    }
}
