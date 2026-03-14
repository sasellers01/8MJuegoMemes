/**
 * Clase que representa un dato real que desmiente un meme.
 * 
 * Cada realidad contiene un identificador, el texto del dato
 * real y la fuente oficial de donde se ha obtenido la información.
 * 
 * @author Sergio S.
 * @version 1.0
 */
package com.iescastelar;
public class Realidad {
	
	private Integer id;
	private String texto;
	private String fuente;
	
	/**
	 * Constructor de la clase Realidad.
	 * 
	 * @param id identificador de la realidad
	 * @param texto texto del dato real
	 * @param fuente fuente oficial de información
	 */
	public Realidad(Integer id, String texto, String fuente) {
		this.id = id;
		this.texto = texto;
		this.fuente = fuente;
	}
	
	/**
	 * Devuelve el identificador de la realidad.
	 * 
	 * @return id de la realidad
	 */
	public Integer getId() {
		return id;
	}
	
	/**
	 * Devuelve el texto del dato real.
	 * 
	 * @return texto de la realidad
	 */
	public String getTexto() {
		return texto;
	}
	
	/**
	 * Devuelve la fuente del dato real.
	 * 
	 * @return fuente de la realidad
	 */
	public String getFuente() {
		return fuente;
	}
	
	/**
	 * Representación en texto de la realidad.
	 * 
	 * @return texto de la realidad con su fuente
	 */
	@Override
	public String toString() {
		return texto + " (Fuente: " + fuente + ")";
	}
}