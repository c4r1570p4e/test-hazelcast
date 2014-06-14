package cl.hazelcast.test;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

public class BackupReadTest {

	private HazelcastInstance instance1;
	private HazelcastInstance instance2;
	private HazelcastInstance instance3;
	private HazelcastInstance client;
	
	private HazelcastInstance[] instances = new HazelcastInstance[3];


	@Before
	public void init() {
		Config config = new Config();
		config.getNetworkConfig().getJoin().getMulticastConfig().setMulticastTimeoutSeconds(2);
		
		instance1 = Hazelcast.newHazelcastInstance(config);
		instance2 = Hazelcast.newHazelcastInstance(config);
		instance3 = Hazelcast.newHazelcastInstance(config);
		
		instances[0] = instance1;
		instances[1] = instance2;
		instances[2] = instance3;

		client = HazelcastClient.newHazelcastClient();
		
	}

	@Test
	public void test() {
		
		//config des backups
		instance1.getConfig().getMapConfig("map1").setBackupCount(0);
		instance1.getConfig().getMapConfig("map1").setAsyncBackupCount(0);
		instance1.getConfig().getMapConfig("map1").setReadBackupData(true);
		//activations des stats
		instance1.getConfig().getMapConfig("map1").setStatisticsEnabled(true);
		
		IMap<String, String> mapInstance1 = instance1.getMap("map1");
		IMap<String, String> mapInstance2 = instance2.getMap("map1");
		IMap<String, String> mapInstance3 = instance3.getMap("map1");
		IMap<String, String> mapClient = client.getMap("map1");


		//ajoute 1 donnée dans le cache
		System.out.println("put");
		mapInstance1.put("cle1", "val1");
		
		//verifie toutes stats a 0
		assertThat(mapInstance1.getLocalMapStats().getHits()).isEqualTo(0);
		assertThat(mapInstance2.getLocalMapStats().getHits()).isEqualTo(0);
		assertThat(mapInstance3.getLocalMapStats().getHits()).isEqualTo(0);
		
		//affichage du nb de backup par instance
		printBackupStats();
		
		System.out.println(" --- instance 1 ---");
		for(int i = 0; i < 200; i++) {
			assertThat(mapInstance1.get("cle1")).isEqualTo("val1");
		}
		printStats();

		System.out.println(" --- instance 2 ---");
		for(int i = 0; i < 200; i++) {
			assertThat(mapInstance2.get("cle1")).isEqualTo("val1");
		}		
		printStats();

		System.out.println(" --- instance 3 ---");
		for(int i = 0; i < 200; i++) {
			assertThat(mapInstance3.get("cle1")).isEqualTo("val1");
		}		
		printStats();

		System.out.println(" --- client ---");
		for(int i = 0; i < 200; i++) {
			assertThat(mapClient.get("cle1")).isEqualTo("val1");
		}		
		printStats();		
		
		
	}
	
	
	private void printStats() {
		int i = 1;
		for (HazelcastInstance instance : instances) {
			IMap<String, String> map = instance.getMap("map1");
			System.out.println("instance " + i  + " : " + map.getLocalMapStats().getHits() );
			i++;
		}
	}
	
	private void printBackupStats() {
		int i = 1;
		for (HazelcastInstance instance : instances) {
			IMap<String, String> map = instance.getMap("map1");
			System.out.println("Backup : instance " + i  + " : " +  map.getLocalMapStats().getBackupEntryCount() );
			i++;
		}
	}	
	
	@After
	public void shudown() {
		client.shutdown();
		instance1.shutdown();
		instance2.shutdown();
		instance3.shutdown();
	}

	
	
	
	
	
}
