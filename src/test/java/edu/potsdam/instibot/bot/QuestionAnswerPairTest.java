package edu.potsdam.instibot.bot;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.potsdam.instibot.rest.QuestionAnswerPair;


public class QuestionAnswerPairTest {

    @Test
    public void testEquality() {
	QuestionAnswerPair oneQAPair = new QuestionAnswerPair("What is your name?", "Das geht dich gar nichts an!");
	QuestionAnswerPair anotherQAPair = new QuestionAnswerPair("What is your name?", "Das geht dich gar nichts an!");
	QuestionAnswerPair aThirdQAPair = new QuestionAnswerPair("What is your name?", "My name is none of your business!");

	assertEquals(true, oneQAPair.equals(anotherQAPair));
	assertEquals(false, oneQAPair.equals(aThirdQAPair));
    }
}
