package edu.potsdam.instibot;

import java.io.IOException;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.bio.SocketConnector;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;

public class JettyStarter {

    private Server server;

    public JettyStarter(int port) throws IOException {

	server = new Server();
	SocketConnector connector = new SocketConnector();

	QueuedThreadPool queuedThreadPool = new QueuedThreadPool(1000);

	server.setThreadPool(queuedThreadPool);

	// Set some timeout options to make debugging easier.
	connector.setMaxIdleTime(1000 * 60 * 60);
	connector.setSoLingerTime(-1);
	connector.setPort(port);
	server.setConnectors(new Connector[] { connector });

	WebAppContext webContext = new WebAppContext(getBaseUrl(), "/");
	webContext.setServer(server);
	webContext.setMaxFormContentSize(3000000);
	server.setHandler(webContext);
    }

    private String getBaseUrl() {
	/*
	if (JettyStarter.class.getClassLoader().getResource(".") == null) {
	    // Running from jar
	    URL webInfUrl = JettyStarter.class.getClassLoader().getResource("WEB-INF");
	    String webInfUrlString = webInfUrl.toExternalForm();
	    return webInfUrlString.substring(0, webInfUrlString.lastIndexOf('/') + 1);
	} else {
	*/
	    // Running from Eclipse
	    return "src/main/webapp";
	/*} */
    }

    public void start() {
	try {
	    System.out
		    .println(">>> STARTING EMBEDDED JETTY SERVER, PRESS ANY KEY TO STOP");
	    server.start();
	    System.in.read();
	    System.out.println(">>> STOPPING EMBEDDED JETTY SERVER");

	    server.stop();
	    server.join();
	} catch (Exception e) {
	    e.printStackTrace();
	    System.exit(100);
	}
    }
}