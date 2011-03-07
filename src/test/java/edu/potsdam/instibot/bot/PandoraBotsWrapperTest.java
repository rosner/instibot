package edu.potsdam.instibot.bot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import edu.potsdam.instibot.rest.UserCredentials;

public class PandoraBotsWrapperTest {

    private static final String NOT_IMPLEMENTED = "Not implemented";
    
    protected static final String ALICE_BOT_ID = "f5d922d97e345aa1";
    protected PandoraBotsWrapper fixture;
    
    @Before
    public void setUp() throws Exception {
	fixture = new PandoraBotsWrapper();
    }
    
    @Test
    public void test_creation() {
	assertNotNull(fixture);
    }
    
    @Test
    public void test_getNewUserId() {
	UserCredentials userCredentials = fixture.getNewUserCredentials(ALICE_BOT_ID);
	assertNotNull(userCredentials);
	assertEquals(true, !userCredentials.getUserId().isEmpty());
	
	UserCredentials otherUserCredentials = fixture.getNewUserCredentials(ALICE_BOT_ID);
	boolean actual = !otherUserCredentials.equals(userCredentials);
	assertEquals(true, actual);
    }
    
    public void test_respondToWrongUserId() {
	fail(NOT_IMPLEMENTED);
    }
    
    public void test_getResponseForRequest() {
	fail(NOT_IMPLEMENTED);
    }
    
    public void test_AliceBot() {
	fail(NOT_IMPLEMENTED);

	String actual = "";
	assertEquals("My name is ALICE.", actual);
    }
    

}
