public class Realidad {
	private int id;
	private String texto;
	private String fuente;
	
	public Realidad(int id, String texto, String fuente) {
		this.id = id;
		this.texto = texto;
		this.fuente = fuente;
	}
	
	public int getId() {
		return id;
	}
	
	public String getTexto() {
		return texto;
	}
	
	public String getFuente() {
		return fuente;
	}
	
	@Override
	public String toString() {
		return texto + " (Fuente: " + fuente + ")";
	}
}