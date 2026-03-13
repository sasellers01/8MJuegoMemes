import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase encargada de gestionar la relación entre memes y realidades.
 * 
 * Lee el fichero XML soluciones.xml y crea un mapa que relaciona
 * el ID de un meme con el ID de la realidad que lo desmiente.
 * 
 * Formato del fichero XML:
 * 
 * <soluciones>
 *     <solucion>
 *         <memeId>1</memeId>
 *         <realidadId>1</realidadId>
 *     </solucion>
 * </soluciones>
 * 
 * @author Sergio S.
 */
public class Soluciones {

    private final String FICHERO_SOLUCIONES = "datos/soluciones.xml";

    /** Mapa que relaciona memeId con realidadId */
    private Map<Integer, Integer> mapaSoluciones;

    /**
     * Constructor de la clase Soluciones.
     */
    public Soluciones() {
        mapaSoluciones = new HashMap<>();
    }

    /**
     * Lee el fichero XML y carga las relaciones meme-realidad.
     */
    public void cargarSoluciones() {

        try {

            File ficheroXML = new File(FICHERO_SOLUCIONES);

            DocumentBuilderFactory factoria = DocumentBuilderFactory.newInstance();
            DocumentBuilder constructor = factoria.newDocumentBuilder();
            Document documento = constructor.parse(ficheroXML);

            NodeList lista = documento.getElementsByTagName("solucion");

            for (int i = 0; i < lista.getLength(); i++) {

                Element solucion = (Element) lista.item(i);

                Integer memeId = Integer.valueOf(
                        solucion.getElementsByTagName("memeId").item(0).getTextContent());

                Integer realidadId = Integer.valueOf(
                        solucion.getElementsByTagName("realidadId").item(0).getTextContent());

                mapaSoluciones.put(memeId, realidadId);
            }

        } catch (Exception e) {
            System.out.println("Error leyendo soluciones: " + e.getMessage());
        }
    }

    /**
     * Devuelve el ID de la realidad correcta para un meme.
     * 
     * @param memeId identificador del meme
     * @return identificador de la realidad correcta
     */
    public Integer getRealidadId(Integer memeId) {
        return mapaSoluciones.get(memeId);
    }
}