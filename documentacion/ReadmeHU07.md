# JuegoMemes.java

Clase principal del juego. Gestiona el flujo completo de una partida: carga los datos de los ficheros, presenta 5 memes al jugador, evalúa sus respuestas y actualiza el ranking de mejores puntuaciones.

---

## ¿Qué hace?

1. **Carga** los memes, realidades y soluciones desde sus ficheros de datos.
2. **Presenta** 5 memes al jugador de forma aleatoria.
3. **Evalúa** cada respuesta marcándola como correcta (✓) o incorrecta (✗).
4. **Gestiona el ranking** top 3 guardado en `mejores.txt`.
5. **Muestra** el resultado final y las mejores puntuaciones.

---

## Constantes

```java
private static final String FICHERO_MEMES      = "datos/memes.txt";
private static final String FICHERO_REALIDADES = "datos/realidades.json";
private static final String FICHERO_SOLUCIONES = "datos/soluciones.xml";
private static final String FICHERO_RANKING    = "mejores.txt";
private static final int    TOTAL_MEMES        = 5;
private static final int    MAX_RANKING        = 3;
```

- `FICHERO_MEMES` — ruta del fichero con los memes.
- `FICHERO_REALIDADES` — ruta del fichero JSON con las realidades científicas.
- `FICHERO_SOLUCIONES` — ruta del fichero XML que empareja cada meme con su realidad.
- `FICHERO_RANKING` — fichero donde se persiste el top 3.
- `TOTAL_MEMES` — número de memes que se presentan por partida.
- `MAX_RANKING` — tamaño máximo del ranking.

---

## Clases internas de modelo

### `Meme`

Representa un meme con su identificador y texto:

```java
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
```

### `Realidad`

Representa la realidad científica que desmiente un meme, incluyendo su fuente:

```java
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
```

---

## Carga de datos

### `cargarMemes()`

Lee `memes.txt` línea a línea. Ignora comentarios (`#`) y líneas vacías. Separa cada línea por `|` para obtener el id y el texto:

```java
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
```

**Formato esperado de `memes.txt`:**
```
1 | Los videojuegos causan violencia
2 | El azúcar hace hiperactivos a los niños
```

### `cargarRealidades()`

Lee `realidades.json` y lo parsea manualmente sin librerías externas. Divide el contenido por `{` para localizar cada objeto y extrae los campos `id`, `realidad` y `fuente`:

```java
static Map<Integer, Realidad> cargarRealidades() throws IOException {
    Map<Integer, Realidad> mapa = new LinkedHashMap<>();
    String contenido = Files.readString(Path.of(FICHERO_REALIDADES));

    String[] objetos = contenido.split("\\{");
    int idActual = -1;

    for (String bloque : objetos) {
        String idStr = extraerJsonValor(bloque, "id");
        if (idStr != null) {
            try { idActual = Integer.parseInt(idStr.trim()); } catch (NumberFormatException ignored) {}
        }
        String texto  = extraerJsonValor(bloque, "realidad");
        String fuente = extraerJsonValor(bloque, "fuente");
        if (idActual != -1 && texto != null && fuente != null) {
            mapa.put(idActual, new Realidad(idActual, texto, fuente));
            idActual = -1;
        }
    }
    return mapa;
}
```

**Formato esperado de `realidades.json`:**
```json
[
  { "id": 1, "correcta": { "realidad": "Ningún estudio lo demuestra", "fuente": "APA 2020" } }
]
```

### `cargarSoluciones()`

Lee `soluciones.xml` y lo parsea manualmente. Divide el contenido por `<solucion>` y extrae las etiquetas `memeId` y `realidadId`:

```java
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
```

**Formato esperado de `soluciones.xml`:**
```xml
<soluciones>
  <solucion><memeId>1</memeId><realidadId>1</realidadId></solucion>
</soluciones>
```

---

## Helpers de parseo

### `extraerJsonValor()`

Localiza una clave JSON dentro de un fragmento de texto y devuelve su valor, ya sea numérico o string:

```java
static String extraerJsonValor(String texto, String clave) {
    int i = texto.indexOf("\"" + clave + "\"");
    if (i == -1) return null;
    int dospuntos = texto.indexOf(":", i + clave.length() + 2);
    if (dospuntos == -1) return null;
    int comilla    = texto.indexOf("\"", dospuntos + 1);
    int comaOLlave = texto.indexOf(",", dospuntos + 1);
    int corchete   = texto.indexOf("}", dospuntos + 1);
    int finNum     = (comaOLlave != -1 && corchete != -1) ? Math.min(comaOLlave, corchete)
                    : (comaOLlave != -1 ? comaOLlave : corchete);
    if (comilla == -1 || (finNum != -1 && finNum < comilla)) {
        if (finNum == -1) return null;
        return texto.substring(dospuntos + 1, finNum).trim(); // numérico
    }
    int inicio = comilla + 1;
    int fin    = texto.indexOf("\"", inicio);
    return (fin > inicio) ? texto.substring(inicio, fin) : null; // string
}
```

### `extraerXml()`

Extrae el contenido entre una etiqueta de apertura y cierre XML:

```java
static String extraerXml(String texto, String etiqueta) {
    int inicio = texto.indexOf("<" + etiqueta + ">");
    int fin    = texto.indexOf("</" + etiqueta + ">");
    if (inicio == -1 || fin == -1) return null;
    return texto.substring(inicio + etiqueta.length() + 2, fin).trim();
}
```

---

## Ranking

### `cargarRanking()`

Lee `mejores.txt` y devuelve las entradas válidas. Si el archivo no existe devuelve lista vacía sin error:

```java
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
```

### `gestionarRanking()`

Comprueba si la puntuación entra en el top 3, pide el nombre si procede, guarda y muestra el ranking:

```java
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
```

La condición de entrada tiene dos casos:
- El ranking **aún no está lleno** (`entradas.size() < MAX_RANKING`).
- La puntuación **supera la peor** del top 3.

### `mostrarRanking()`

Imprime la tabla del ranking por consola:

```java
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
```

**Ejemplo de salida:**
```
══ TOP 3 MEJORES PUNTUACIONES ══
  1. Alice                5
  2. Bob                  4
  3. Carol                3
═════════════════════════════
```

---

## Flujo del `main`

```java
// 1. Cargar los tres ficheros de datos
memes      = cargarMemes();
realidades = cargarRealidades();
soluciones = cargarSoluciones();

// 2. Bucle de 5 memes aleatorios
for (int ronda = 1; ronda <= TOTAL_MEMES; ronda++) {
    int memeId      = ids.get(random.nextInt(ids.size()));
    Meme meme       = memes.get(memeId);
    int realId      = soluciones.getOrDefault(memeId, -1);
    Realidad realidad = realidades.get(realId);

    System.out.println("\n  » " + meme.texto + "\n");
    String respuesta = sc.nextLine().trim();

    boolean acierto = realidad != null
            && respuesta.equalsIgnoreCase(realidad.texto);

    if (acierto) { puntos++; }
}

// 3. Gestionar ranking y cerrar
gestionarRanking(puntos, sc);
```

La comparación de respuestas usa `equalsIgnoreCase` para no penalizar diferencias de mayúsculas/minúsculas.

---

## Ficheros de datos

| Fichero                  | Formato       | Descripción                                        |
|--------------------------|---------------|----------------------------------------------------|
| `datos/memes.txt`        | `id \| texto` | Lista de memes, uno por línea                      |
| `datos/realidades.json`  | JSON          | Realidades con texto y fuente, parseadas manualmente |
| `datos/soluciones.xml`   | XML           | Relaciona cada `memeId` con su `realidadId`        |
| `mejores.txt`            | `nombre;punt` | Ranking top 3, se crea si no existe                |

---

## Comportamiento ante errores

| Situación                          | Código responsable                                      | Comportamiento                          |
|------------------------------------|---------------------------------------------------------|-----------------------------------------|
| Fichero de datos no existe         | `catch (IOException e)` en `main`                      | Imprime error y termina el programa     |
| Algún mapa cargado vacío           | `if (memes.isEmpty() \|\| ...)`                         | Informa y termina sin lanzar excepción  |
| Línea malformada en memes.txt      | `if (partes.length < 2) continue`                       | La línea se ignora                      |
| Etiqueta XML incompleta            | `if (inicio == -1 \|\| fin == -1) return null`          | Devuelve null, la entrada se ignora     |
| Error al guardar el ranking        | `catch (IOException e)` en `gestionarRanking`           | Imprime el error y continúa             |

---

## Tests

Los tests se encuentran en `JuegoMemesTest.java` y cubren:

- Que `cargarMemes()` lee correctamente el formato `id | texto` e ignora comentarios y líneas vacías
- Que `cargarRealidades()` parsea el JSON manual correctamente
- Que `cargarSoluciones()` parsea el XML manual correctamente
- Que `extraerJsonValor()` extrae valores string y numéricos, y devuelve null si la clave no existe
- Que `extraerXml()` extrae el contenido entre etiquetas y devuelve null si faltan
- Que `cargarRanking()` devuelve lista vacía si no existe el archivo e ignora líneas malformadas
- Que `gestionarRanking()` guarda correctamente, elimina la peor puntuación y mantiene el orden
- Que los ids de soluciones son coherentes con los memes y realidades cargados
- Que el ranking nunca supera `MAX_RANKING` tras múltiples partidas
