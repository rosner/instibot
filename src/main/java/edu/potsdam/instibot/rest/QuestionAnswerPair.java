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
}
