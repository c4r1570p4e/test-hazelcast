package cl.hazelcast.db;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.h2.engine.ConnectionInfo;
import org.h2.engine.Engine;
import org.h2.engine.Session;
import org.h2.tools.RunScript;
import org.h2.tools.Server;

public class H2DB {

	public static void main(String[] args) {

		Server server = null;

		try {
			server = Server.createTcpServer("-tcpPort", "9123", "-tcpAllowOthers", "-webAllowOthers", "-pgAllowOthers",
					"-trace");
			server.start();

			System.out.println("serveur démarré : " + server.getURL());

			
            Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost:9123/~/test;IFEXISTS=TRUE");	
            
            RunScript.execute(conn, new FileReader("src/main/resources/script.sql"));
			

			System.out.println("Pour arret [Entrée]");
			try {
				System.in.read();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} finally {
			if (server != null && server.isRunning(false)) {
				server.stop();
			}
		}

	}

}
