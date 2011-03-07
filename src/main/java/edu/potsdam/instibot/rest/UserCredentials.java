package edu.potsdam.instibot.rest;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.joda.time.DateTime;

@XmlRootElement
public class UserCredentials {
    
    protected String userId;
    
    protected String path;
    
    protected DateTime expirationDate;

    public UserCredentials() {
	// TODO Auto-generated constructor stub
    }

    @XmlElement
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @XmlElement
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @XmlElement
    public String getExpirationDateAsString() {
        return expirationDate.toString();
    }
    
    public DateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(DateTime expirationDate) {
        this.expirationDate = expirationDate;
    }       
}
