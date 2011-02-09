package edu.potsdam.instibot.bot;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BotTest {

    protected Bot fixture;

    @Before
    public void setUp() throws Exception {
	fixture = new Bot();
    }

    @Test
    public void testCreation() {
	assertNotNull(fixture);
    }
    
    @Test
    public void testName() {
	String botName = "Instibot 3000"; 
	fixture.setName(botName);
	assertEquals(botName, fixture.getName());
	
    }

}
