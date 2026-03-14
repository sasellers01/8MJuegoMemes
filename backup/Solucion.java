/**
 * Clase que representa la relación entre un meme
 * y la realidad que lo desmiente.
 * 
 * Se utiliza para saber qué realidad corresponde
 * a cada meme durante el juego.
 * 
 * @author Sergio S.
 * @version 1.0
 */
public class Solucion {
	
	private Integer memeId;
	private Integer realidadId;
	
	/**
	 * Constructor de la clase Solucion.
	 * 
	 * @param memeId identificador del meme
	 * @param realidadId identificador de la realidad correcta
	 */
	public Solucion(Integer memeId, Integer realidadId) {
		this.memeId = memeId;
		this.realidadId = realidadId;
	}
	
	/**
	 * Devuelve el identificador del meme.
	 * 
	 * @return id del meme
	 */
	public Integer getMemeId() {
		return memeId;
	}
	
	/**
	 * Devuelve el identificador de la realidad correcta.
	 * 
	 * @return id de la realidad
	 */
	public Integer getRealidadId() {
		return realidadId;
	}
}