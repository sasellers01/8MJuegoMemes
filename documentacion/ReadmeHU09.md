# Ranking.java — HU09

Clase encargada de gestionar el ranking de mejores puntuaciones al finalizar una partida. Comprueba si la puntuación del jugador entra en el top 3, guarda su nombre si es así, y muestra la lista actualizada.

---

## ¿Qué hace?

1. **Comprueba** si la puntuación obtenida entra en el top 3.
2. **Pide el nombre** al usuario si su puntuación merece estar en el ranking.
3. **Guarda** nombre y puntuación en `mejores.txt`, ordenado de mayor a menor.
4. **Muestra** la lista de las mejores puntuaciones por consola.

---

## Constantes

```java
private static final String ARCHIVO = "mejores.txt";
private static final int MAX_PUNTUACIONES = 3;
```

- `ARCHIVO` — ruta del fichero donde se persiste el ranking entre partidas.
- `MAX_PUNTUACIONES` — tamaño máximo del ranking (top 3).

---

## Método principal

```java
public static void gestionarRanking(int puntuacionActual);
```

Es el único método público. Recibe la puntuación obtenida al finalizar la partida y coordina todo el flujo:

```java
public static void gestionarRanking(int puntuacionActual) {
    List<int[]> mejores = cargarMejores();

    boolean entraEnRanking = mejores.size() < MAX_PUNTUACIONES
            || puntuacionActual > mejores.get(mejores.size() - 1)[0];

    if (entraEnRanking) {
        System.out.println("\n¡Enhorabuena! Tu puntuación de " + puntuacionActual
                + " está entre las tres mejores.");
        System.out.print("Introduce tu nombre: ");
        Scanner sc = new Scanner(System.in);
        String nombre = sc.nextLine().trim();

        guardarMejores(nombre, puntuacionActual);
    } else {
        System.out.println("\nTu puntuación: " + puntuacionActual
                + ". No has entrado en el top " + MAX_PUNTUACIONES + ".");
        mostrarRanking();
    }
}
```

La condición de entrada al ranking tiene dos casos:
- El ranking **aún no está lleno** (`mejores.size() < MAX_PUNTUACIONES`).
- La puntuación **supera la peor** del top 3 (`puntuacionActual > mejores.get(mejores.size() - 1)[0]`).

---

## Lectura del archivo

`cargarEntradas()` lee `mejores.txt` línea a línea y devuelve las entradas válidas:

```java
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
                if (partes.length == 2) {  // solo acepta "nombre;puntuacion"
                    entradas.add(partes);
                }
            }
        }
    } catch (IOException e) {
        System.err.println("Error al leer " + ARCHIVO + ": " + e.getMessage());
    }

    return entradas;
}
```

- Si el archivo **no existe**, lo crea vacío y devuelve lista vacía.
- Solo acepta líneas con exactamente **dos partes** separadas por `;`.
- Las líneas vacías o malformadas se **ignoran** silenciosamente.

`cargarMejores()` usa `cargarEntradas()` pero extrae solo las puntuaciones como enteros para poder compararlas:

```java
private static List<int[]> cargarMejores() {
    List<String[]> entradas = cargarEntradas();
    List<int[]> puntuaciones = new ArrayList<>();
    for (String[] e : entradas) {
        try {
            // Guardamos en [0], que es el único índice del array
            puntuaciones.add(new int[]{Integer.parseInt(e[1].trim())});
        } catch (NumberFormatException ex) {
            // línea malformada, se ignora
        }
    }
    // Ordenar de mayor a menor
    puntuaciones.sort((a, b) -> b[0] - a[0]);
    return puntuaciones;
}
```

---

## Guardado del ranking

`guardarMejores()` recarga las entradas completas, añade la nueva, ordena y recorta al top 3:

```java
private static void guardarMejores(String nombre, int puntuacion) {
    List<String[]> entradas = cargarEntradas();

    // Añadir la nueva entrada
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
```

---

## Visualización del ranking

Hay dos versiones de `mostrarRanking()`: una pública que lee del archivo, y una privada que recibe la lista directamente:

```java
public static void mostrarRanking() {
    mostrarRanking(cargarEntradas()); // lee del archivo
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
```

**Ejemplo de salida:**

```
=== TOP 3 MEJORES PUNTUACIONES ===
  1. Alice                300
  2. Bob                  200
  3. Carol                100
================================
```

---

## Archivo de datos

| Archivo       | Descripción                                                                 |
|---------------|-----------------------------------------------------------------------------|
| `mejores.txt` | Persiste el ranking entre partidas. Se crea automáticamente si no existe.   |

**Formato de cada línea:**
```
nombre;puntuacion
```

**Ejemplo:**
```
Alice;300
Bob;200
Carol;100
```

---

## Comportamiento ante errores

| Situación                    | Código responsable                                      | Comportamiento                          |
|------------------------------|---------------------------------------------------------|-----------------------------------------|
| `mejores.txt` no existe      | `archivo.createNewFile()`                               | Lo crea vacío y continúa               |
| Error al crear el archivo    | `catch (IOException e)` en `cargarEntradas`             | Imprime el error y devuelve lista vacía |
| Línea malformada al leer     | `if (partes.length == 2)`                               | La línea se ignora                      |
| Puntuación no numérica       | `catch (NumberFormatException ex)`                      | La entrada se ignora                    |
| Error al escribir el archivo | `catch (IOException e)` en `guardarMejores`             | Imprime el error y continúa             |

---

## Cómo usarlo

Al final del juego, llamar con la puntuación obtenida:

```java
Ranking.gestionarRanking(puntuacionFinal);
```

Para mostrar el ranking en cualquier momento sin gestionar puntuación:

```java
Ranking.mostrarRanking();
```

---

## Tests

Los tests se encuentran en `RankingTest.java` y cubren:

- Que `cargarEntradas()` crea el archivo si no existe y devuelve lista vacía
- Que se leen correctamente líneas con formato `nombre;puntuacion`
- Que las líneas malformadas se ignoran
- Que `cargarMejores()` devuelve las puntuaciones ordenadas de mayor a menor
- Que `gestionarRanking()` felicita al usuario si entra en el top 3
- Que `gestionarRanking()` guarda correctamente la nueva entrada
- Que una puntuación inferior al top 3 completo no modifica el archivo
- Que la peor puntuación es eliminada cuando entra una mejor
- Que `mostrarRanking()` muestra el encabezado y los registros correctamente
