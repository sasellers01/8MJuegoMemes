import org.json.*;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

public class Realidad {
	private int id;
	private String texto;
	private String fuente;
	
	public Realidad(int id, String texto, String fuente) {
		this.id = id;
		this.texto = texto;
		this.fuente = fuente;
	}
	
	public int getId() { return id; }
	public String getTexto() { return texto; }
	public String getFuente() { return fuente; }
	
	@Override
	public String toString() {
		return texto + " (Fuente: " + fuente + ")";
	}
}

class Realidades {
	
	private final String FICHERO_REALIDADES = "datos/realidades.json";
	private Map<Integer, Realidad> mapaRealidades;
	
	public Realidades() {
		mapaRealidades = new HashMap<>();
	}
	
	public void cargarRealidades() {
		try {
			// Leer todo el fichero como String
			Path path = Paths.get(FICHERO_REALIDADES);
			String contenido = new String(Files.readAllBytes(path));
			
			// Convertir a JSONArray
			JSONArray lista = new JSONArray(contenido);
			
			for (int i = 0; i < lista.length(); i++) {
				JSONObject obj = lista.getJSONObject(i);
				int id = obj.getInt("id");
				
				// Solo usamos "correcta" por ahora
				JSONObject correcta = obj.getJSONObject("correcta");
				String texto = correcta.getString("realidad");
				String fuente = correcta.getString("fuente");
				
				mapaRealidades.put(id, new Realidad(id, texto, fuente));
			}
		
		} catch (Exception e) {
			System.out.println("ERROR al leer realidades: " + e.getMessage());
		}
	}
	
	// Obtener realidad por ID
	public Realidad getRealidad(int id) {
		return mapaRealidades.get(id);
	}
	
	// Para pruebas
	public Map<Integer, Realidad> getMapaRealidades() {
		return mapaRealidades;
	}
}