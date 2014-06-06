package cl.hazelcast.client;

import java.io.IOException;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.XmlClientConfigBuilder;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

/**
 * Created by clannoy on 31/05/2014.
 */
public class Client {

	public static void main(String[] args) {
		ClientConfig clientConfig = null;
		try {
			clientConfig = new XmlClientConfigBuilder("myHazelcast-client.xml").build();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
		IMap map = client.getMap("map1");
		System.out.println("Map Size:" + map.size());
		
		map.put("1", "value1");
		

	}

}
