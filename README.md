# 🎮 8M Juego Memes

Juego educativo en Java que desmiente bulos sobre **igualdad de género** mediante una dinámica de preguntas y respuestas. El jugador debe identificar qué afirmación real desmiente cada meme sexista mostrado en pantalla.

Desarrollado como proyecto de aula en el marco del **8 de Marzo, Día Internacional de la Mujer**.

---

## 👥 Autores

| Nombre | GitHub |
|--------|--------|
| Sergio Seller | [@sasellers01](https://github.com/sasellers01) |
| Adrián Tena Gallardo | https://github.com/Adriantena22 |
| Sergio González | — |

---

## 🕹️ Cómo funciona el juego

1. El sistema muestra un meme con un bulo sobre igualdad de género
2. El jugador elige cuál de las dos afirmaciones mostradas es la **realidad documentada** que lo desmiente
3. Se juegan **5 rondas** y se acumula puntuación
4. Si la puntuación está entre las **3 mejores**, se guarda en el ranking

---

## ⚙️ Requisitos

- Java 17 o superior
- Sin dependencias externas — solo el JDK estándar

---

## 🚀 Instalación y ejecución

### 1. Clonar el repositorio
```bash
git clone https://github.com/sasellers01/8MJuegoMemes.git
cd 8MJuegoMemes
```

### 2. Compilar
```bash
javac src/SistemaMemes.java
```

### 3. Ejecutar
```bash
java -cp src SistemaMemes
```

---

## 📁 Estructura del proyecto

```
8MJuegoMemes/
├── src/                    → Código fuente Java
│   └── SistemaMemes.java
├── datos/                  → Ficheros de datos del juego
│   ├── memes.txt           → Los 15 memes (bulos) del juego
│   ├── realidades.json     → Afirmaciones correctas y falsas con fuentes
│   └── soluciones.xml      → Relación meme → realidad correcta
├── resultados/             → Ranking de mejores puntuaciones
│   └── mejores.txt
├── documentacion/          → Manuales del proyecto
│   ├── ManualInstalacion.md
│   ├── ManualUsuario.md
│   └── javadoc/            → Documentación generada automáticamente
└── README.md
```

---

## 📄 Formato de los ficheros de datos

**`datos/memes.txt`** — formato `ID|texto`:
```
1|Vuelve a la cocina (la mujer pertenece al espacio doméstico...)
2|Ok Charo (término para deslegitimar la opinión de las mujeres...)
```

**`datos/realidades.json`** — cada entrada con `id`, `correcta` y `falsa`:
```json
{
  "id": 1,
  "correcta": { "realidad": "...", "fuente": "Fuente oficial" },
  "falsa":    { "realidad": "...", "fuente": "Fuente inventada" }
}
```

**`datos/soluciones.xml`** — relación numérica memeId → realidadId:
```xml
<solucion><memeId>1</memeId><realidadId>1</realidadId></solucion>
```

---

## 📚 Fuentes de los datos

Los memes y realidades están documentados con fuentes académicas reales, entre ellas:

- Investigación UANL 2022 — Ludivina Cantú Ortiz
- Revista Virtualis 2020
- Teknokultura UCM 2021
- Informe Instituto de las Mujeres de España 2025
- El Salto Diario 2025
- Verificat 2025 / Universidad de Amsterdam

---

## 📝 Licencia

MIT License — ver fichero [LICENSE](LICENSE)
