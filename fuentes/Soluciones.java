private Map<Integer, Integer> mapaSoluciones; // memeId -> realidadId

public void cargarSoluciones() {
	mapaSoluciones = new HashMap<>();
	try {
		File file = new File(FICHERO_SOLUCIONES);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(file);
		
		NodeList nodos = doc.getElementsByTagName("solucion");
		for (int i = 0; i < nodos.getLength(); i++) {
			Node nodo = nodos.item(i);
			NodeList hijos = nodo.getChildNodes();
			int memeId = -1, realidadId = -1;
			for (int j = 0; j < hijos.getLength(); j++) {
				Node hijo = hijos.item(j);
				if (hijo.getNodeName().equals("memeId")) memeId = Integer.parseInt(hijo.getTextContent());
				if (hijo.getNodeName().equals("realidadId")) realidadId = Integer.parseInt(hijo.getTextContent());
			}
			if (memeId != -1 && realidadId != -1) {
				mapaSoluciones.put(memeId, realidadId);
			}
		}
	} catch (Exception e) {
		System.out.println("ERROR al leer soluciones: " + e.getMessage());
	}
}