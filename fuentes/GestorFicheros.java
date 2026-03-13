import java.io.File;
import java.io.IOException;

/**
 * Clase encargada de comprobar la existencia de los directorios y ficheros
 * necesarios para el funcionamiento del programa.
 * 
 * Verifica la existencia del directorio de datos y sus ficheros, y también
 * el directorio de resultados. Si el fichero de resultados no existe,
 * lo crea automáticamente.
 * 
 * Forma parte de las historias de usuario HU1 y HU2.
 * 
 * @author Sergio
 */
public class GestorFicheros {
	
	private final String DIR_DATOS = "datos";
	private final String DIR_RESULTADOS = "resultados";
	
	private final String FICHERO_MEMES = "datos/memes.txt";
	private final String FICHERO_REALIDADES = "datos/realidades.json";
	private final String FICHERO_SOLUCIONES = "datos/soluciones.xml";
	private final String FICHERO_MEJORES = "resultados/mejores.txt";
	
	/**
	 * Comprueba que existan los directorios y ficheros necesarios
	 * para ejecutar la aplicación.
	 * 
	 * Si el fichero de resultados no existe, se crea automáticamente.
	 * 
	 * @return true si todos los ficheros necesarios existen o se crean
	 *         correctamente, false en caso contrario.
	 */
	public boolean verificarDatos() {
		
		boolean todoCorrecto = true;
		
		File dirDatos = new File(DIR_DATOS);
		if (!dirDatos.exists() || !dirDatos.isDirectory()) {
			System.out.println("ERROR: No existe el directorio datos.");
			todoCorrecto = false;
		}
		
		if (!new File(FICHERO_MEMES).exists()) {
			System.out.println("ERROR: No existe el fichero memes.txt.");
			todoCorrecto = false;
		}
		
		if (!new File(FICHERO_REALIDADES).exists()) {
			System.out.println("ERROR: No existe el fichero realidades.json.");
			todoCorrecto = false;
		}
		
		if (!new File(FICHERO_SOLUCIONES).exists()) {
			System.out.println("ERROR: No existe el fichero soluciones.xml.");
			todoCorrecto = false;
		}
		
		File dirResultados = new File(DIR_RESULTADOS);
		if (!dirResultados.exists()) {
			dirResultados.mkdir();
		}
		
		File mejores = new File(FICHERO_MEJORES);
		if (!mejores.exists()) {
			try {
				mejores.createNewFile();
			} catch (IOException e) {
				System.out.println("ERROR al crear mejores.txt");
				todoCorrecto = false;
			}
		}
		
		return todoCorrecto;
	}
}