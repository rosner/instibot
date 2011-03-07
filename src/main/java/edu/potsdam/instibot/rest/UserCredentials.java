package edu.potsdam.instibot.rest;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

import org.joda.time.DateTime;

@Data @XmlRootElement(name="user_credentials")
public class UserCredentials {
    
    protected String userId;
    
    protected String path;
    
    protected DateTime expirationDate;    
    
}
