package com.iescastelar;
public class Main {
	public static void main(String[] args) {
		GestorFicheros gestor = new GestorFicheros();
		if (!gestor.verificarDatos()) {
			System.out.println("Faltan ficheros o directorios. El programa se detiene.");
			return; // Detiene del programa
		}
		
		System.out.println("Todos los ficheros y directorios están correctos. Se puede continuar con el juego.");
	}
}
