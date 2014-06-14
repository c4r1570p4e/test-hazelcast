package cl.hazelcast.test;

import java.io.Serializable;

import lombok.Data;

@Data
public class RecetteGateau implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String nom;
	private String recette;
	private long tempsDeCuisson;

}
