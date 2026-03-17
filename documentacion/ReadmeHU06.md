# ComprobarRespuesta.java — HU06

Clase encargada de leer la respuesta del usuario, validarla y comprobar si ha acertado. Actualiza la puntuación y muestra el resultado por consola.

## ¿Qué hace?
- Lee la respuesta del usuario (1, 2 o 3).
- Valida que sea un número dentro del rango permitido.
- Comprueba si coincide con la opción correcta devuelta por HU5.
- Si acierta, suma un punto y muestra mensaje de correcto.
- Si falla, muestra la realidad correcta con su fuente académica.

## Método principal
```java
static Integer comprobarRespuesta(Scanner scanner, Integer opcionCorrecta,
                                  Realidad realidad, Integer puntuacion);
```

### Parámetros
- `scanner` — escáner de entrada estándar
- `opcionCorrecta` — número de opción correcta devuelto por HU5
- `realidad` — objeto con la realidad correcta y su fuente
- `puntuacion` — puntuación antes de esta ronda

### Devuelve
La nueva puntuación tras evaluar la respuesta.

## Validación de la entrada
```java
Integer respuesta = -1;
while (respuesta < 1 || respuesta > 3) {
    try {
        respuesta = Integer.parseInt(scanner.nextLine().trim());
        if (respuesta < 1 || respuesta > 3)
            System.out.print("Escribe 1, 2 o 3: ");
    } catch (NumberFormatException e) {
        System.out.print("Escribe 1, 2 o 3: ");
    }
}
```
El bucle se repite hasta que el usuario introduce un valor válido (1, 2 o 3).

## Comprobación de la respuesta
```java
if (respuesta.equals(opcionCorrecta)) {
    System.out.println("\n  ✓  ¡CORRECTO!");
    puntuacion++;
} else {
    System.out.println("\n  ✗  INCORRECTO");
    System.out.println("  La realidad correcta era:");
    System.out.println("  → " + realidad.textoCorrecto);
    System.out.println("     Fuente: " + realidad.fuenteCorrecta);
}
```

## Ejemplo de salida correcta
```
  ✓  ¡CORRECTO!
```

## Ejemplo de salida incorrecta
```
  ✗  INCORRECTO
  La realidad correcta era:
  → Las prácticas machistas en internet refuerzan la subordinación de la mujer
     Fuente: Investigación UANL 2022 - Ludivina Cantú Ortiz
```

## Comportamiento ante errores
| Situación | Comportamiento |
|-----------|----------------|
| El usuario escribe una letra | `NumberFormatException` capturada, se vuelve a pedir |
| El usuario escribe un número fuera de rango | Se vuelve a pedir la entrada |

## Tests
Los tests se encuentran en `ComprobarRespuestaTest.java` y cubren:
- Que una respuesta correcta suma un punto
- Que una respuesta incorrecta no suma punto
- Que una respuesta inválida (fuera de rango) es rechazada

## Autor
Adrián Tena Gallardo
