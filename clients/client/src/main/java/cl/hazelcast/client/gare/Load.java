package cl.hazelcast.client.gare;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;

import au.com.bytecode.opencsv.CSVReader;
import cl.hazelcast.common.gare.Gare;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.XmlClientConfigBuilder;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

public class Load {

	public static void main(String[] args) {
		ClientConfig clientConfig = null;
		try {
			clientConfig = new XmlClientConfigBuilder("myHazelcast-client.xml").build();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		HazelcastInstance client = null;

		try {
			client = HazelcastClient.newHazelcastClient(clientConfig);
			IMap<Long, Gare> cache = client.getMap("gares");

			try (CSVReader reader = new CSVReader(new FileReader("src\\main\\resources\\datas\\gares.csv"), ';')) {
				String[] nextLine;
				while ((nextLine = reader.readNext()) != null) {

					Gare gare = new Gare();
					gare.setId(Long.parseLong(nextLine[0]));
					gare.setCodeLigne(Long.parseLong(nextLine[1]));
					gare.setNom(nextLine[2]);
					gare.setNature(nextLine[3]);
					gare.setLatitude(new BigDecimal(nextLine[4].replace(',', '.')));
					gare.setLongitude(new BigDecimal(nextLine[5].replace(',', '.')));

					cache.put(gare.getId(), gare);
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.exit(-1);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}

		} finally {
			if (client != null) {
				client.shutdown();
			}
		}

	}
}
