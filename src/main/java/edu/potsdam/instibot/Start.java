package edu.potsdam.instibot;

import java.io.IOException;

public class Start {

    public static void main(String[] args) throws IOException {
	JettyStarter jettyStarter = new JettyStarter(58080);
	jettyStarter.start();
    }
}
