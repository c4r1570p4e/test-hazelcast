package cl.hazelcast.test;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

public class TrucALaConQuIlFautSavoirTest {

	private HazelcastInstance instance;

	@Before
	public void init() {
		Config config = new Config();
		config.getNetworkConfig().getJoin().getMulticastConfig().setMulticastTimeoutSeconds(0);
		instance = Hazelcast.newHazelcastInstance(config);
	}

	@After
	public void shudown() {
		instance.shutdown();
	}

	/**
	 * bien que le cache soit une map il n'a pas le même comportement qu'une map
	 */
	@Test
	public void la_mise_en_cache_des_objets_est_effectuee_par_valeur() {

		Map<String, RecetteGateau> lesRecettesDeGateau = instance.getMap("lesRecettesDeGateau"); //new HashMap<String, RecetteGateau>();

		RecetteGateau recetteOriginal = new RecetteGateau();
		recetteOriginal.setNom("Tarte aux pommes");
		recetteOriginal.setRecette("Une tarte avec des pommes dedans");
		recetteOriginal.setTempsDeCuisson(20);

		lesRecettesDeGateau.put("pommes", recetteOriginal);

		RecetteGateau recette1 = lesRecettesDeGateau.get("pommes");

		assertThat(recetteOriginal).isEqualTo(recette1);

		assertThat(recetteOriginal).isNotSameAs(recette1);

		recetteOriginal.setNom("original");
		recette1.setNom("recette1");

		assertThat(recetteOriginal.getNom()).isEqualTo("original");
		assertThat(recette1.getNom()).isEqualTo("recette1");

		RecetteGateau recetteOriginalQuiVientDeLaMap = lesRecettesDeGateau.get("pommes");
		assertThat(recetteOriginalQuiVientDeLaMap.getNom()).isEqualTo("Tarte aux pommes");

	}

	@Test
	public void l_egalite_et_le_hash_de_la_cle_se_font_sur_le_resultat_de_sa_serialisation() {

		CleComposee cle1 = new CleComposee(1, 1);
		CleComposee cle2 = new CleComposee(1, 2);

		assertThat(cle1).isEqualTo(cle2);

		Map<CleComposee, String> map = instance.getMap("mapDeTest"); //new HashMap<CleComposee, String>();
		map.put(cle1, "cle1");
		map.put(cle2, "cle2");

		assertThat(map.size()).isEqualTo(2);
		assertThat(map.get(cle1)).isEqualTo("cle1");
		assertThat(map.get(cle2)).isEqualTo("cle2");
	}

}
