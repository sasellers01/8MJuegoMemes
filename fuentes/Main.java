/**
 * Clase principal del programa.
 * 
 * Se encarga de iniciar la aplicación comprobando primero
 * que los ficheros necesarios existen.
 * 
 * Si todo es correcto, carga los datos del juego.
 */
public class Main {

    public static void main(String[] args) {

        System.out.println("=== INICIO DEL PROGRAMA ===");

        GestorFicheros gestor = new GestorFicheros();

        if (!gestor.verificarDatos()) {
            System.out.println("No se puede iniciar el programa.");
            return;
        }

        System.out.println("Ficheros verificados correctamente.");

        Memes memes = new Memes();
        Realidades realidades = new Realidades();
        Soluciones soluciones = new Soluciones();

        memes.cargarMemes();
        realidades.cargarRealidades();
        soluciones.cargarSoluciones();

        System.out.println("Datos cargados correctamente.");

        Meme meme = memes.obtenerMemeAleatorio();

        System.out.println("\nMEME:");
        System.out.println(meme);

        Integer realidadId = soluciones.getRealidadId(meme.getId());

        Realidad realidad = realidades.getRealidad(realidadId);

        System.out.println("\nREALIDAD CORRECTA:");
        System.out.println(realidad);

        System.out.println("\n=== FIN DEL PROGRAMA ===");
    }
}