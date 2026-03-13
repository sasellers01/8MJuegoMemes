import org.json.*;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase encargada de gestionar los datos reales del juego.
 * 
 * Lee el fichero JSON realidades.json y crea una colección de objetos
 * {@link Realidad}.
 * 
 * Formato del fichero JSON:
 * [
 *   {
 *     "id": 1,
 *     "correcta": {
 *       "realidad": "Texto del dato real",
 *       "fuente": "Fuente oficial"
 *     }
 *   }
 * ]
 * 
 * Solo se utiliza el objeto "correcta", ya que representa el dato real
 * que desmiente el meme.
 * 
 * @author Sergio S.
 */
public class Realidades {

    private final String FICHERO_REALIDADES = "datos/realidades.json";

    /** Mapa que almacena las realidades usando su ID como clave */
    private Map<Integer, Realidad> mapaRealidades;

    /**
     * Constructor de la clase Realidades.
     */
    public Realidades() {
        mapaRealidades = new HashMap<>();
    }

    /**
     * Lee el fichero JSON y carga las realidades en memoria.
     */
    public void cargarRealidades() {

        try {

            Path path = Paths.get(FICHERO_REALIDADES);
            String contenido = new String(Files.readAllBytes(path));

            JSONArray lista = new JSONArray(contenido);

            for (int i = 0; i < lista.length(); i++) {

                JSONObject objeto = lista.getJSONObject(i);

                Integer id = objeto.getInt("id");

                JSONObject correcta = objeto.getJSONObject("correcta");

                String texto = correcta.getString("realidad");
                String fuente = correcta.getString("fuente");

                mapaRealidades.put(id, new Realidad(id, texto, fuente));
            }

        } catch (Exception e) {
            System.out.println("Error leyendo realidades: " + e.getMessage());
        }
    }

    /**
     * Devuelve una realidad a partir de su identificador.
     * 
     * @param id identificador de la realidad
     * @return objeto Realidad correspondiente
     */
    public Realidad getRealidad(Integer id) {
        return mapaRealidades.get(id);
    }
}