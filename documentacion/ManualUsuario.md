\# Manual de Usuario



\## Introducción

\*\*8M Juego Memes\*\* es una aplicación educativa que presenta bulos sobre igualdad de género en forma de memes. El jugador debe elegir, entre varias opciones, la realidad documentada que desmiente cada meme.



\## Inicio del juego

Al ejecutar la aplicación, se muestra una pantalla de bienvenida y se cargan automáticamente los datos desde la carpeta `datos/`. Si todo es correcto, aparece el primer meme.



!\[Pantalla de inicio](inicio.png)  

\*Figura 1: Pantalla de bienvenida y carga de datos.\*



\## Desarrollo de una partida



\### 1. Presentación del meme y opciones

Se muestra un meme (bulo) y una lista numerada con tres posibles realidades. Solo una de ellas es la correcta.



!\[Meme con opciones](meme\_opciones.png)  

\*Figura 2: Ejemplo de meme y sus tres opciones.\*



\### 2. Elección del jugador

El usuario introduce el número correspondiente a la opción que considera correcta (por ejemplo, `3`) y pulsa Enter.



!\[Elección del usuario](eleccion.png)  

\*Figura 3: Introduciendo el número de la opción elegida.\*



\### 3. Retroalimentación

\- Si la respuesta es \*\*correcta\*\*, aparece un mensaje de acierto y se suma un punto.

\- Si es \*\*incorrecta\*\*, se muestra la realidad correcta con su fuente.



!\[Resultado correcto](acierto.png)  

\*Figura 4: Mensaje de acierto.\*



!\[Resultado incorrecto](error.png)  

\*Figura 5: Mensaje de error y realidad correcta.\*



\### 4. Marcador parcial

Después de cada ronda, se muestra el marcador actualizado (puntos acumulados / rondas jugadas).



!\[Marcador parcial](marcador.png)  

\*Figura 6: Marcador tras una ronda.\*



\## Final de la partida

Tras completar las 5 rondas, se muestra la puntuación total obtenida.



!\[Puntuación final](puntuacion\_final.png)  

\*Figura 7: Resultado final de la partida.\*



\## Registro en el ranking

Si la puntuación está entre las tres mejores almacenadas, el programa solicita el nombre del jugador y lo guarda en el ranking.



!\[Solicitud de nombre](pedir\_nombre.png)  

\*Figura 8: Pidiendo nombre para el ranking.\*



A continuación, se muestra la lista actualizada de las mejores puntuaciones.



!\[Ranking](ranking.png)  

\*Figura 9: Top 3 de mejores puntuaciones.\*



\## Fin del programa

El juego se despide y finaliza.



!\[Despedida](despedida.png)  

\*Figura 10: Mensaje de despedida.\*



\## Notas adicionales

\- El archivo de ranking se guarda en `resultados/mejores.txt` y puede consultarse manualmente.

\- Si no hay partidas previas, el ranking aparece vacío hasta que se registre la primera puntuación.

