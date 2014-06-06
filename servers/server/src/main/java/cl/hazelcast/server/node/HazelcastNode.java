package cl.hazelcast.server.node;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by clannoy on 31/05/2014.
 */
public class HazelcastNode {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    }

}
