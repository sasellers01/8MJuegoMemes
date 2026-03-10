import java.io.File;
import java.io.IOException;

/**
 * Clase que comprueba y valida el directorio datos y fichero de resultados.
 * 
 * @author Sergio Alejandro Seller Svidenko
 * @version 1.0
 */
public class GestorFicheros {

    // Rutas de los directorios y ficheros
    private final String DIR_DATOS = "datos";
    private final String DIR_RESULTADOS = "resultados";
    private final String FICHERO_MEMES = "datos/memes.txt";
    private final String FICHERO_REALIDADES = "datos/realidades.json";
    private final String FICHERO_SOLUCIONES = "datos/soluciones.xml";
    private final String FICHERO_MEJORES = "resultados/mejores.txt";

    /**
     * Verifica que existan los directorios y ficheros necesarios.
     * Si falta algo, informa al usuario y retorna false.
	 * 
	 * @return Resultado del verificador de datos
     */
    public boolean verificarDatos() {
        boolean todoBien = true;

        // Comprueba directorio datos
        File dirDatos = new File(DIR_DATOS);
        if (!dirDatos.exists() || !dirDatos.isDirectory()) {
            System.out.println("ERROR: No existe el directorio '" + DIR_DATOS + "'");
            todoBien = false;
        }

        // Comprueba ficheros de datos
        File memes = new File(FICHERO_MEMES);
        File realidades = new File(FICHERO_REALIDADES);
        File soluciones = new File(FICHERO_SOLUCIONES);

        if (!memes.exists()) {
            System.out.println("ERROR: No existe el fichero de memes: " + FICHERO_MEMES);
            todoBien = false;
        }

        if (!realidades.exists()) {
            System.out.println("ERROR: No existe el fichero de realidades: " + FICHERO_REALIDADES);
            todoBien = false;
        }

        if (!soluciones.exists()) {
            System.out.println("ERROR: No existe el fichero de soluciones: " + FICHERO_SOLUCIONES);
            todoBien = false;
        }

        // Comprueba directorio resultados
        File dirResultados = new File(DIR_RESULTADOS);
        if (!dirResultados.exists()) {
            System.out.println("No existe el directorio '" + DIR_RESULTADOS + "'. Creándolo...");
            if (dirResultados.mkdir()) {
                System.out.println("Directorio '" + DIR_RESULTADOS + "' creado correctamente.");
            } else {
                System.out.println("ERROR: No se pudo crear el directorio '" + DIR_RESULTADOS + "'");
                todoBien = false;
            }
        }

        // Comprueba fichero mejores.txt, si no existe lo crea
        File mejores = new File(FICHERO_MEJORES);
        if (!mejores.exists()) {
            System.out.println("No existe el fichero '" + FICHERO_MEJORES + "'. Creándolo...");
            try {
                if (mejores.createNewFile()) {
                    System.out.println("Fichero '" + FICHERO_MEJORES + "' creado correctamente.");
                } else {
                    System.out.println("ERROR: No se pudo crear el fichero '" + FICHERO_MEJORES + "'");
                    todoBien = false;
                }
            } catch (IOException e) {
                System.out.println("ERROR al crear el fichero '" + FICHERO_MEJORES + "': " + e.getMessage());
                todoBien = false;
            }
        }

        return todoBien;
    }
}