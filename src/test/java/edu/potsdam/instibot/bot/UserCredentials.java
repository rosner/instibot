package edu.potsdam.instibot.bot;

import org.joda.time.DateTime;

import lombok.Data;

@Data
public class UserCredentials {

    protected String userId;
    
    protected String path;
    
    protected DateTime expirationDate;    
    
}
