package cl.hazelcast.test;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = { "id1"})
public class CleComposee implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id1;
	private int id2;

	public CleComposee(int id1, int id2) {
		super();
		this.id1 = id1;
		this.id2 = id2;
	}

}
