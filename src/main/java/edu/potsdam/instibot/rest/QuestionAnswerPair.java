package edu.potsdam.instibot.rest;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class QuestionAnswerPair {

    protected String question;
    
    protected String answer;
    
    public QuestionAnswerPair() {
    }
    
    public QuestionAnswerPair(String question, String answer) {
	super();
	this.question = question;
	this.answer = answer;
    }

    @XmlElement
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @XmlElement
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
    
    @Override
    public boolean equals(Object obj) {
	if (obj != null && obj instanceof QuestionAnswerPair) {
	    QuestionAnswerPair questionAnswerPair = (QuestionAnswerPair) obj;
	    return question.equals(questionAnswerPair.question) && answer.equals(questionAnswerPair.answer);
	} else {
	    return false;
	}
    }
    
    @Override
    public String toString() {
        return "Q: " + question + " A: " + answer;
    }
}
