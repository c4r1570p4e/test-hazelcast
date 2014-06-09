package cl.hazelcast.server.node;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import cl.hazelcast.server.map.interceptor.LogMapInterceptor;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IAtomicLong;
import com.hazelcast.core.IMap;

/**
 * Created by clannoy on 31/05/2014.
 */
@Slf4j
public class HazelcastNode {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");

		HazelcastInstance hazelcastInstance = (HazelcastInstance) ctx.getBean("instance");

		IAtomicLong cpt = hazelcastInstance.getAtomicLong("cptLogMapInterceptor");

		if (cpt.compareAndSet(0, 1)) {
			log.debug("Init interceptor");
			addLogMapInterceptor(hazelcastInstance, "map1");
			addLogMapInterceptor(hazelcastInstance, "map2");
			addLogMapInterceptor(hazelcastInstance, "map3");
			addLogMapInterceptor(hazelcastInstance, "map4");
			addLogMapInterceptor(hazelcastInstance, "map5");


		}

	}

	protected static void addLogMapInterceptor(HazelcastInstance hazelcastInstance, String mapName) {
		IMap<String, String> map1 = hazelcastInstance.getMap(mapName);
		map1.addInterceptor(new LogMapInterceptor(mapName));
	}

}
