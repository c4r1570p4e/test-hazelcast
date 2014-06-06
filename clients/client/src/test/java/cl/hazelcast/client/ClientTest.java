package cl.hazelcast.client;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.XmlClientConfigBuilder;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

public class ClientTest {

	private static HazelcastInstance client = null;

	@BeforeClass
	public static void init() throws Exception {
		ClientConfig clientConfig = null;
		clientConfig = new XmlClientConfigBuilder("myHazelcast-client.xml").build();
		client = HazelcastClient.newHazelcastClient(clientConfig);

	}

	@AfterClass
	public static void clean() throws Exception {
		if(client != null) {
			client.shutdown();
		}
	}

	@Test
	public void test() throws Exception {
		IMap<String,String> map = client.getMap("map1");
		map.put("1", "value1");
		assertThat(map.get("1")).isEqualTo("value1");
	}

}
