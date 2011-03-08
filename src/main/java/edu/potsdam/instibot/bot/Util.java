package edu.potsdam.instibot.bot;

import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import edu.potsdam.instibot.rest.UserCredentials;

public class Util {

    // Wed, 16 Mar 2011 20:52:42 GMT
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'").withLocale(Locale.ENGLISH);
    
    public static UserCredentials getCredentialsFromString(String inputString) {

	String[] cookieValues = inputString.split(";");

	UserCredentials userCredentials = new UserCredentials();

	for (String cookieValue : cookieValues) {
	    cookieValue = cookieValue.trim();
	    int valueBeginIndex = cookieValue.indexOf("=");
	    if (valueBeginIndex >= 0) {
		String actualValue = cookieValue.substring(valueBeginIndex + 1, cookieValue.length()).trim();

		if (cookieValue.startsWith(PandoraBotsWrapper.COOKIES_KEY_USER_ID)) {
		    userCredentials.setUserId(actualValue);
		}
		if (cookieValue.startsWith(PandoraBotsWrapper.COOKIES_KEY_EXP_DATE)) {
		    
		    DateTime parseDateTime = DATE_FORMATTER.parseDateTime(actualValue);
		    String expDateAsString = parseDateTime.toString();
		    userCredentials.setExpirationDate(expDateAsString);
		}
		if (cookieValue.startsWith(PandoraBotsWrapper.COOKIES_KEY_PATH)) {
		    userCredentials.setPath(actualValue);
		}
	    }
	}
	
	if (userCredentials.getUserId().isEmpty()) {
	    return null;
	}
	
	return userCredentials;
    }
}