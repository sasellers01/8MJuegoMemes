# Manual de Instalación — 8M Juego Memes

## Requisitos del sistema

| Requisito | Versión mínima |
|-----------|----------------|
| Java JDK  | 17 o superior  |
| Sistema operativo | Windows 10, macOS 12, Linux |
| Espacio en disco | < 1 MB |
| Conexión a internet | Solo para clonar el repositorio |

Para comprobar si tienes Java instalado abre una terminal y escribe:
```bash
java -version
```
Si no lo tienes, descárgalo desde: https://adoptium.net

---

## Pasos para instalar y ejecutar

### 1. Obtener el código fuente

**Opción A — Con Git:**
```bash
git clone https://github.com/sasellers01/8MJuegoMemes.git
cd 8MJuegoMemes
```

**Opción B — Sin Git:**
1. Ve a https://github.com/sasellers01/8MJuegoMemes
2. Haz clic en **Code → Download ZIP**
3. Descomprime el ZIP en la carpeta que quieras
4. Abre una terminal y navega hasta esa carpeta

---

### 2. Compilar el programa
```bash
javac fuentes/com/iescastelar/JuegoMemes.java
```

Si la compilación es correcta no aparece ningún mensaje de error.

---

### 3. Ejecutar el programa
```bash
java -cp fuentes JuegoMemes
```

---

### 4. Ejecutar los tests
```bash
javac -d . test/SistemaMemesTest.java
java -ea com.iescastelar.SistemaMemesTest

javac -d . test/ComprobarRespuestaTest.java
java -ea com.iescastelar.ComprobarRespuestaTest

javac -d . test/RankingTest.java
java -ea com.iescastelar.RankingTest
```

---

## Desinstalar

Borra la carpeta del proyecto. No se instala nada en el sistema.
