import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Meme {
	
	private int id;
	private String texto;
	
	public Meme(int id, String texto) {
		this.id = id;
		this.texto = texto;
	}
	
	public int getId() { return id; }
	public String getTexto() { return texto; }
	
	@Override
	public String toString() {
		return id + ": " + texto;
	}
}

class Memes {
	
	private final String FICHERO_MEMES = "datos/memes.txt";
	private Map<Integer, Meme> mapaMemes;
	private Random random;
	
	public Memes() {
		mapaMemes = new HashMap<>();
		random = new Random();
	}
	
	// Carga los memes del fichero
	public void cargarMemes() {
		try (BufferedReader br = new BufferedReader(new FileReader(FICHERO_MEMES))) {
			String linea;
			while ((linea = br.readLine()) != null) {
				linea = linea.trim();
				if (linea.isEmpty() || linea.startsWith("#")) continue;
				
				String[] partes = linea.split("\\|", 2);
				int id = Integer.parseInt(partes[0]);
				String texto = partes[1];
				
				mapaMemes.put(id, new Meme(id, texto));
			}
		} catch (Exception e) {
			System.out.println("ERROR al leer memes: " + e.getMessage());
		}
	}
	
	// Devuelve un meme aleatorio
	public Meme obtenerMemeAleatorio() {
		if (mapaMemes.isEmpty()) return null;
		Object[] keys = mapaMemes.keySet().toArray();
		int key = (int) keys[random.nextInt(keys.length)];
		return mapaMemes.get(key);
	}
	
	// Obtener meme por ID
	public Meme getMeme(int id) {
		return mapaMemes.get(id);
	}
	
	// Para pruebas
	public Map<Integer, Meme> getMapaMemes() {
		return mapaMemes;
	}
}