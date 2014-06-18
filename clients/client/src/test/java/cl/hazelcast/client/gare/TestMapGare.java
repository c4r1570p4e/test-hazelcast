package cl.hazelcast.client.gare;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Collection;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import cl.hazelcast.common.gare.Gare;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.XmlClientConfigBuilder;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import com.hazelcast.query.SqlPredicate;

public class TestMapGare {

	private static HazelcastInstance client = null;

	@BeforeClass
	public static void init() throws Exception {
		ClientConfig clientConfig = null;
		clientConfig = new XmlClientConfigBuilder("myHazelcast-client.xml").build();
		client = HazelcastClient.newHazelcastClient(clientConfig);

	}

	@AfterClass
	public static void clean() throws Exception {
		if (client != null) {
			client.shutdown();
		}
	}

	private IMap<Long, Gare> getMapGare() {
		return client.getMap("gares");
	}

	@Test
	public void la_taille_de_la_map_est_bien_la_bonne() {
		assertThat(getMapGare().size()).isEqualTo(6442);
	}

	@Test
	public void recherche_nom() {
		
		Predicate predicate = Predicates.equal("nom", "Lille-Europe");
		Collection<Gare> trouve = getMapGare().values(predicate);

		assertThat(trouve.size()).isEqualTo(1);
		assertThat(trouve.iterator().next().getNom()).isEqualTo("Lille-Europe");

	}

	@Test
	public void recherche_nom_ilike() {

		Predicate predicate = Predicates.ilike("nom", "%lille%");
		Collection<Gare> trouve = getMapGare().values(predicate);

		assertThat(trouve.size()).isEqualTo(17);

		for (Gare gare : trouve) {
			assertThat(gare.getNom()).containsIgnoringCase("lille");
		}

	}

	@Test
	public void recherche_gare_voyageur() {

		Predicate predicate = Predicates.like("nature", "%Voyageur%");
		Collection<Gare> trouve = getMapGare().values(predicate);

		assertThat(trouve.size()).isEqualTo(3464);

	}

	@Test
	public void recherche_gare_lille_voyageur_en_mode_sql() {

		Predicate predicate = new SqlPredicate("nom ILIKE '%lille%' AND nature ILIKE '%voyageur%'");
		Collection<Gare> trouve = getMapGare().values(predicate);
		
		assertThat(trouve.size()).isEqualTo(8);
		
	}
	
}
