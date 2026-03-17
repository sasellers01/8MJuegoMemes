# SistemaMemes.java — HU05

Clase encargada de mostrar un meme al azar y una lista numerada de realidades para que el usuario elija cuál desmiente el bulo.

## ¿Qué hace?
- Filtra los memes que ya han salido para evitar repeticiones.
- Elige un meme al azar de los disponibles.
- Busca la realidad que corresponde a ese meme.
- Mezcla aleatoriamente la opción correcta y la falsa.
- Muestra el meme y las opciones numeradas por consola.
- Devuelve el número de opción correcta para que HU6 lo compruebe.

## Constantes
No tiene constantes propias. Usa los datos cargados por HU3 y HU4.

## Método principal
```java
static Integer mostrarMemeYRealidades(Map<Integer, String> memes,
                                      List<Realidad> realidades,
                                      List<Integer> memesUsados,
                                      Integer puntuacion);
```

### Parámetros
- `memes` — mapa con todos los memes cargados (id → texto)
- `realidades` — lista con todas las realidades cargadas
- `memesUsados` — lista de IDs ya mostrados, se actualiza en este método
- `puntuacion` — puntuación actual para mostrar el marcador

### Devuelve
El número de opción correcta (1 o 2) para que HU6 lo compruebe.

## Lógica de selección sin repetición
```java
List<Integer> disponibles = new ArrayList<>(memes.keySet());
disponibles.removeAll(memesUsados);

Random random = new Random();
Integer memeId = disponibles.get(random.nextInt(disponibles.size()));
memesUsados.add(memeId);
```
- Se obtienen todos los IDs disponibles.
- Se eliminan los ya usados.
- Se elige uno al azar y se añade a la lista de usados.

## Mezcla de opciones
```java
List<String> opciones = new ArrayList<>();
opciones.add(realidad.textoCorrecto);
opciones.add(realidad.textoFalso);
Collections.shuffle(opciones, random);
```
Las opciones se mezclan aleatoriamente para que la correcta no esté siempre en la misma posición.

## Ejemplo de salida
```
══════════════════════════════════════════
  Puntuacion actual: 2
══════════════════════════════════════════

MEME: Vuelve a la cocina (la mujer pertenece al espacio doméstico...)

Que afirmacion desmiente este meme?

  1. Los estudios demuestran que las mujeres prefieren el ámbito doméstico
  2. Las prácticas machistas que relegan a la mujer al espacio doméstico refuerzan su subordinación
```

## Archivo de datos usado
| Fichero | Descripción |
|---------|-------------|
| `datos/memes.txt` | Contiene los 15 memes del juego |
| `datos/realidades.json` | Contiene las realidades correctas y falsas |

## Comportamiento ante errores
| Situación | Comportamiento |
|-----------|----------------|
| No quedan memes disponibles | `disponibles` estará vacío, el juego no debería llegar aquí |
| Realidad no encontrada | `realidad` será null, se evita con el bucle de búsqueda |

## Tests
Los tests se encuentran en `SistemaMemesTest.java` y cubren:
- Que no se repite un meme ya usado
- Que el método devuelve 1 o 2 como opción válida

## Autor
Adrián Tena Gallardo
