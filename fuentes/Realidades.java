private Map<Integer, Realidad> mapaRealidades;

public void cargarRealidades() {
	mapaRealidades = new HashMap<>();
	try (FileReader reader = new FileReader(FICHERO_REALIDADES)) {
		Gson gson = new Gson();
		List<JsonObject> lista = gson.fromJson(reader, new TypeToken<List<JsonObject>>(){}.getType());
		for (JsonObject obj : lista) {
			int id = obj.get("id").getAsInt();
			JsonObject correcta = obj.getAsJsonObject("correcta");
			String texto = correcta.get("realidad").getAsString();
			String fuente = correcta.get("fuente").getAsString();
			mapaRealidades.put(id, new Realidad(id, texto, fuente));
		}
	} catch (Exception e) {
		System.out.println("ERROR al leer realidades: " + e.getMessage());
	}
}