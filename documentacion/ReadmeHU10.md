# Despedida.java

Clase encargada de cerrar la partida mostrando al jugador las mejores puntuaciones registradas y despidiéndose.

---

## ¿Qué hace?

Al finalizar una partida, esta clase realiza dos acciones en orden:

1. **Muestra el ranking** — lee el archivo `mejores.txt` y presenta por consola la lista de hasta 3 mejores puntuaciones con su nombre y puntuación.
2. **Se despide** — imprime un mensaje de despedida al jugador.

---

## Constantes

La clase define dos constantes que controlan su comportamiento:

```java
private static final String ARCHIVO = "mejores.txt";
private static final int MAX_PUNTUACIONES = 3;
```

- `ARCHIVO` — ruta del fichero donde están guardadas las puntuaciones.
- `MAX_PUNTUACIONES` — número máximo de entradas que se muestran en el ranking.

---

## Método principal

```java
public static void mostrarRankingYDespedirse();
```

Es el único método público de la clase. No recibe parámetros ni devuelve nada. Se llama una sola vez al final del juego.

Internamente hace dos cosas seguidas: mostrar el ranking y luego imprimir la despedida:

```java
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
```

**Ejemplo de salida por consola:**

```
=== TOP 3 MEJORES PUNTUACIONES ===
  1. Alice                300
  2. Bob                  200
  3. Carol                100
================================

¡Gracias por jugar! Hasta la próxima. 👋
```

Si todavía no hay ninguna puntuación guardada:

```
=== TOP 3 MEJORES PUNTUACIONES ===
  (sin registros aún)
================================

¡Gracias por jugar! Hasta la próxima. 👋
```

---

## Lectura del archivo

El método privado `cargarEntradas()` se encarga de leer `mejores.txt` línea a línea:

```java
private static List<String[]> cargarEntradas() {
    List<String[]> entradas = new ArrayList<>();
    File archivo = new File(ARCHIVO);

    if (!archivo.exists()) {
        return entradas; // devuelve lista vacía sin lanzar error
    }

    try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            linea = linea.trim();
            if (!linea.isEmpty()) {
                String[] partes = linea.split(";");
                if (partes.length == 2) {   // solo acepta líneas con formato "nombre;puntuacion"
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

Puntos clave:
- Si el archivo **no existe**, devuelve una lista vacía sin lanzar ninguna excepción.
- Cada línea se divide por `;` — solo se aceptan líneas con exactamente dos partes (`nombre` y `puntuacion`).
- Las líneas vacías o malformadas se ignoran silenciosamente.

---

## Archivo de datos

| Archivo       | Descripción                              |
|---------------|------------------------------------------|
| `mejores.txt` | Contiene el ranking guardado. Si no existe, la clase lo trata como ranking vacío sin lanzar ningún error. |

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

| Situación                        | Código responsable                                           | Comportamiento                          |
|----------------------------------|--------------------------------------------------------------|-----------------------------------------|
| `mejores.txt` no existe          | `if (!archivo.exists()) return entradas;`                    | Devuelve lista vacía, no lanza error    |
| Línea malformada                 | `if (partes.length == 2)`                                    | La línea se ignora                      |
| Error de lectura                 | `catch (IOException e) { System.err.println(...) }`         | Imprime el error y continúa             |

---

## Cómo integrarlo en el juego

Al final del flujo principal del juego, después de gestionar el ranking, añadir:

```java
Despedida.mostrarRankingYDespedirse();
```

---

## Tests

Los tests se encuentran en `DespedidaTest.java` y cubren:

- Que el encabezado `TOP 3` aparece siempre
- Que se muestra `(sin registros aún)` cuando no hay datos
- Que los nombres y puntuaciones se muestran correctamente
- Que el mensaje de despedida aparece siempre y después del ranking
- Que no se lanza ninguna excepción si el archivo no existe
- Que las líneas malformadas se ignoran
- Que la numeración de posiciones es correcta
