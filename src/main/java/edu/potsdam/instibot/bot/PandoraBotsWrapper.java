package edu.potsdam.instibot.bot;

import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;

public class PandoraBotsWrapper {

    private static final Logger LOGGER = Logger.getLogger(PandoraBotsWrapper.class);

    private static final String PANDORA_BOTS_BASE_URL = "http://pandorabots.com/pandora/talk";

    private static final String HTTP_HEADER_COOKIE_KEY = "set-cookie";

    public static final String COOKIES_KEY_USER_ID = "botcust2";

    public static final String COOKIES_KEY_EXP_DATE = "expires";

    public static final String COOKIES_KEY_PATH = "path";

    protected WebResource pandoraBotsBaseResource;

    public PandoraBotsWrapper() {
	pandoraBotsBaseResource = Client.create().resource(
		PANDORA_BOTS_BASE_URL);
    }

    public UserCredentials getNewUserCredentials(String botId) {
	WebResource botResource = pandoraBotsBaseResource.queryParam("botid", botId);
	ClientResponse response = botResource.post(ClientResponse.class);
	UserCredentials userCredentials = null;
	LOGGER.debug(String.format("Requested pandora bot with id %s under the following url %s", botId, response));
	// extract the new user id from the raw http header because the pandora
	// server is an old lisp based beasty
	if (response.getClientResponseStatus() == Status.OK) {
	    MultivaluedMap<String, String> headers = response.getHeaders();
	    List<String> cookieList = headers.get(HTTP_HEADER_COOKIE_KEY);
	    if (cookieList.size() > 0) {
		userCredentials = Util.getCredentialsFromString(cookieList.get(0));
	    }
	}

	return userCredentials;
    }

}
