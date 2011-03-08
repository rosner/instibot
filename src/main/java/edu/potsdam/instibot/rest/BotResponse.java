package edu.potsdam.instibot.rest;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BotResponse {
    
    protected List<QuestionAnswerPair> responses;
    
    public BotResponse() {
    }

    public List<QuestionAnswerPair> getResponses() {
        return responses;
    }

    public void setResponses(List<QuestionAnswerPair> responses) {
        this.responses = responses;
    }
}
