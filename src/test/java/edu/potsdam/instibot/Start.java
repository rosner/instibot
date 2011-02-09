package edu.potsdam.instibot;

public class Start {

    public static void main(String[] args) {
	JettyStarter jettyStarter = new JettyStarter(8080);
	jettyStarter.start();
    }
}
