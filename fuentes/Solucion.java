import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Soluciones {
	
	private final String FICHERO_SOLUCIONES = "datos/soluciones.xml";
	private Map<Integer, Integer> mapaSoluciones; // memeId -> realidadId
	
	public Soluciones() {
		mapaSoluciones = new HashMap<>();
	}
	
	public void cargarSoluciones() {
		try {
			File file = new File(FICHERO_SOLUCIONES);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(file);
			
			NodeList nodos = doc.getElementsByTagName("solucion");
			for (int i = 0; i < nodos.getLength(); i++) {
				Node nodo = nodos.item(i);
				NodeList hijos = nodo.getChildNodes();
				int memeId = -1;
				int realidadId = -1;
				for (int j = 0; j < hijos.getLength(); j++) {
					Node hijo = hijos.item(j);
					if (hijo.getNodeName().equals("memeId")) memeId = Integer.parseInt(hijo.getTextContent().trim());
					if (hijo.getNodeName().equals("realidadId")) realidadId = Integer.parseInt(hijo.getTextContent().trim());
				}
				if (memeId != -1 && realidadId != -1) {
					mapaSoluciones.put(memeId, realidadId);
				}
			}
		} catch (Exception e) {
			System.out.println("ERROR al leer soluciones: " + e.getMessage());
		}
	}
	
	// Obtener ID de la realidad correcta para un meme
	public int getRealidadId(int memeId) {
		return mapaSoluciones.getOrDefault(memeId, -1);
	}
	
	// Para pruebas
	public Map<Integer, Integer> getMapaSoluciones() {
		return mapaSoluciones;
	}
}