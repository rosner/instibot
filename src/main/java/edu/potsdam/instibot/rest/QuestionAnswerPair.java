package edu.potsdam.instibot.rest;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class QuestionAnswerPair {

    protected String question;
    
    protected String answer;
    
    public QuestionAnswerPair() {
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
