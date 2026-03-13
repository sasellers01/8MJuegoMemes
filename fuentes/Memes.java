import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Clase encargada de gestionar los memes del juego.
 * 
 * Lee el fichero de texto memes.txt y crea una colección de objetos
 * {@link Meme}. Los memes se almacenan en un Map usando su ID como clave.
 * 
 * Formato del fichero memes.txt:
 * ID|TEXTO_DEL_MEME
 * 
 * Ejemplo:
 * 1|Vuelve a la cocina
 * 2|Ok Charo
 * 
 * Las líneas que empiezan por # se consideran comentarios y se ignoran.
 * 
 * @author Sergio S.
 */
public class Memes {

    private final String FICHERO_MEMES = "datos/memes.txt";

    /** Mapa que almacena los memes usando su ID como clave */
    private Map<Integer, Meme> mapaMemes;

    /** Generador de números aleatorios para seleccionar memes */
    private Random random;

    /**
     * Constructor de la clase Memes.
     * Inicializa la estructura de datos donde se almacenarán los memes.
     */
    public Memes() {
        mapaMemes = new HashMap<>();
        random = new Random();
    }

    /**
     * Lee el fichero memes.txt y carga los memes en memoria.
     */
    public void cargarMemes() {
        try (BufferedReader br = new BufferedReader(new FileReader(FICHERO_MEMES))) {

            String linea;

            while ((linea = br.readLine()) != null) {

                linea = linea.trim();

                if (linea.isEmpty() || linea.startsWith("#"))
                    continue;

                String[] partes = linea.split("\\|", 2);

                Integer id = Integer.parseInt(partes[0]);
                String texto = partes[1];

                mapaMemes.put(id, new Meme(id, texto));
            }

        } catch (Exception e) {
            System.out.println("Error leyendo memes: " + e.getMessage());
        }
    }

    /**
     * Devuelve un meme aleatorio del conjunto de memes cargados.
     * 
     * @return un objeto Meme seleccionado aleatoriamente
     */
    public Meme obtenerMemeAleatorio() {

        Object[] claves = mapaMemes.keySet().toArray();
        Integer clave = (Integer) claves[random.nextInt(claves.length)];

        return mapaMemes.get(clave);
    }

    /**
     * Devuelve un meme a partir de su identificador.
     * 
     * @param id identificador del meme
     * @return objeto Meme correspondiente
     */
    public Meme getMeme(Integer id) {
        return mapaMemes.get(id);
    }
}