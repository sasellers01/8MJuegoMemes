private Map<Integer, Meme> mapaMemes;

public void cargarMemes() {
	mapaMemes = new HashMap<>();
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