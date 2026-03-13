/**
 * Clase que representa un meme dentro del juego.
 * 
 * Cada meme contiene un identificador único y el texto
 * del bulo o frase que será mostrado al usuario.
 * 
 * @author Sergio S.
 */
public class Meme {
	
	private Integer id;
	private String texto;
	
	/**
	 * Constructor de la clase Meme.
	 * 
	 * @param id identificador único del meme
	 * @param texto texto del meme
	 */
	public Meme(Integer id, String texto) {
		this.id = id;
		this.texto = texto;
	}
	
	/**
	 * Devuelve el identificador del meme.
	 * 
	 * @return id del meme
	 */
	public Integer getId() {
		return id;
	}
	
	/**
	 * Devuelve el texto del meme.
	 * 
	 * @return texto del meme
	 */
	public String getTexto() {
		return texto;
	}
	
	/**
	 * Representación en texto del meme.
	 * 
	 * @return cadena con id y texto del meme
	 */
	@Override
	public String toString() {
		return id + " - " + texto;
	}
}