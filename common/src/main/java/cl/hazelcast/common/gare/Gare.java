package cl.hazelcast.common.gare;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = { "id" })
public class Gare implements Serializable {

	private static final long serialVersionUID = 9168607764484666862L;

	private long id;
	private long codeLigne;
	private String nom;
	private String nature;
	private BigDecimal latitude;
	private BigDecimal longitude;

}
