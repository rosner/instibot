package edu.potsdam.instibot.bot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import edu.potsdam.instibot.rest.BotResponse;
import edu.potsdam.instibot.rest.QuestionAnswerPair;
import edu.potsdam.instibot.rest.UserCredentials;

public class PandoraBotsWrapper {

    private static final Logger LOGGER = Logger
	    .getLogger(PandoraBotsWrapper.class);

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
	WebResource botResource = pandoraBotsBaseResource.queryParam("botid",
		botId);
	ClientResponse response = botResource.post(ClientResponse.class);
	UserCredentials userCredentials = null;
	LOGGER.debug(String.format(
		"Requested pandora bot with id %s under the following url %s",
		botId, response));
	// extract the new user id from the raw http header because the pandora
	// server is an old lisp based beasty
	if (response.getClientResponseStatus() == Status.OK) {
	    MultivaluedMap<String, String> headers = response.getHeaders();
	    List<String> cookieList = headers.get(HTTP_HEADER_COOKIE_KEY);
	    if (cookieList.size() > 0) {
		userCredentials = Util.getCredentialsFromString(cookieList
			.get(0));
	    }
	}

	return userCredentials;
    }

    public BotResponse getBotResponse(String botId, String userId,
	    String userInput) {
	MultivaluedMapImpl formData = new MultivaluedMapImpl();
	formData.add(COOKIES_KEY_USER_ID, userId);
	formData.add("input", userInput);

	MultivaluedMapImpl queryParams = new MultivaluedMapImpl();
	queryParams.add("botid", botId);
	// this is specifically for alice. Change this or remove this later
	// queryParams.add("skin", "custom_input");
	WebResource botResource = pandoraBotsBaseResource
		.queryParams(queryParams);

	ClientResponse response = botResource.type(
		MediaType.APPLICATION_FORM_URLENCODED).post(
		ClientResponse.class, formData);

	BotResponse botResponse = null;
	if (response.getClientResponseStatus() == Status.OK) {
	    MultivaluedMap<String, String> headers = response.getHeaders();
	    List<String> contentTypeValues = headers.get("Content-Type");
	    for (String currentValue : contentTypeValues) {
		// get the encoding out of the header.
		Pattern encodingPattern = Pattern.compile("charset=(.*);?",
			Pattern.DOTALL);
		Matcher matcher = encodingPattern.matcher(currentValue);
		if (matcher.find()) {
		    String encoding = matcher.group(1);
		    InputStream entityInputStream = response
			    .getEntityInputStream();
		    String htmlString;
		    try {
			htmlString = getHtmlFromInputStream(entityInputStream,
				encoding);
			entityInputStream.close();
			List<QuestionAnswerPair> extractedPairs = extractQuestionAnswerPairsFromHtml(htmlString);
			botResponse = new BotResponse();
			botResponse.setResponses(extractedPairs);
		    } catch (IOException e) {
			e.printStackTrace();
		    }
		}

	    }

	}

	return botResponse;
    }

    private String getHtmlFromInputStream(InputStream inputStream,
	    String encoding) throws IOException {
	InputStreamReader inputStreamReader = new InputStreamReader(
		inputStream, encoding);
	BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	StringBuilder stringBuilder = new StringBuilder();
	String line;
	while ((line = bufferedReader.readLine()) != null) {
	    stringBuilder.append(line);
	}
	bufferedReader.close();
	inputStreamReader.close();
	return stringBuilder.toString();
    }

    private List<QuestionAnswerPair> extractQuestionAnswerPairsFromHtml(String htmlString) {
	LOGGER.debug(htmlString);
	
	List<QuestionAnswerPair> resultList = new ArrayList<QuestionAnswerPair>();

	Pattern pattern = Pattern.compile("Human\\:.*?\\s(.+?)\\<.*?[\\w+?]\\:.*?\\s(.+?)\\<",
		Pattern.CASE_INSENSITIVE);
	Matcher matcher = pattern.matcher(htmlString);

	while (matcher.find()) {
	    String human = matcher.group(1).trim();
	    String bot = matcher.group(2).trim();

	    QuestionAnswerPair pair = new QuestionAnswerPair(human, bot);
	    resultList.add(pair);
	}

	return resultList;
    }
}
