package edu.potsdam.instibot.bot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    /** Cached response pairs. The Pandora service seems to shorten the responses after a while so that not all repsonse pairs 
     * are available for the client. So we have to keep track. */
    protected Map<String, List<QuestionAnswerPair>> cachedResponsesByUserId = new HashMap<String, List<QuestionAnswerPair>>();

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

    /**
     * Gets a response from the bot with the given id for the given user input.
     * 
     * @param botId The id of the bot from Pandora Service
     * @param userId The user id
     * @param userInput The question from the user
     * @return
     */
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
			List<QuestionAnswerPair> extractedPairs = extractQuestionAnswerPairsFromHtml(htmlString, userId);
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

    /**
     * Simply extracts the QA pairs from the html string and returns 
     * @param htmlString
     * @param userId 
     * @return
     */
    private List<QuestionAnswerPair> extractQuestionAnswerPairsFromHtml(
	    String htmlString, String userId) {
	LOGGER.debug(htmlString);

	List<QuestionAnswerPair> resultList = new ArrayList<QuestionAnswerPair>();

	Pattern pattern = Pattern.compile(
		"Human\\:.*?\\s(.+?)\\<.*?[\\w+?]\\:.*?\\s(.+?)\\<",
		Pattern.CASE_INSENSITIVE);
	Matcher matcher = pattern.matcher(htmlString);

	while (matcher.find()) {
	    String human = matcher.group(1).trim();
	    String bot = matcher.group(2).trim();

	    QuestionAnswerPair pair = new QuestionAnswerPair(human, bot);
	    resultList.add(pair);
	}
	System.out.println(resultList);
	updateQuestionAnswerCache(userId, resultList);

	return cachedResponsesByUserId.get(userId);
    }

    /**
     * Just sync the cached list of QA pairs and the newly ones. It seems the pandora server doesn't always return the full communication
     * NOTE: Could get very bad!
     * @param userId
     * @param questionAnswerPairs
     */
    private void updateQuestionAnswerCache(String userId, List<QuestionAnswerPair> questionAnswerPairs) {
	/*
	[Q: Hallo Chatbot A: Hallo!  Wo wohnst Du?]
	[Q: Ich wohne in Berlin, wo wohnst du A: Erzahle mir ein wenig ueber, Q: Hallo Chatbot A: Hallo!  Wo wohnst Du?]
	[Q: Wie heisst du eigentlich A: Ich heisse  foo., Q: Ich wohne in Berlin, wo wohnst du A: Erzahle mir ein wenig ueber, Q: Hallo Chatbot A: Hallo!  Wo wohnst Du?]
	[Q: Das ist aber ein schoener Name A: Hm...erzaehl mir mehr..., Q: Wie heisst du eigentlich A: Ich heisse  foo., Q: Ich wohne in Berlin, wo wohnst du A: Erzahle mir ein wenig ueber, Q: Hallo Chatbot A: Hallo!  Wo wohnst Du?]
	[Q: Kennst du den Geschmack von Haribos A: Leider nein., Q: Das ist aber ein schoener Name A: Hm...erzaehl mir mehr..., Q: Wie heisst du eigentlich A: Ich heisse  foo., Q: Ich wohne in Berlin, wo wohnst du A: Erzahle mir ein wenig ueber]
	[Q: Wie alt bist du A: Schwer zu sagen. Bei Programmen gibt es da andere Massstaebe. Zum ersten Mal aktiviert wurde ich am, Q: Kennst du den Geschmack von Haribos A: Leider nein., Q: Das ist aber ein schoener Name A: Hm...erzaehl mir mehr..., Q: Wie heisst du eigentlich A: Ich heisse  foo.]
	*/
	List<QuestionAnswerPair> cachedPairs = cachedResponsesByUserId.get(userId);

	if ( cachedPairs != null ) {
	    
	    QuestionAnswerPair lastCachedPair = cachedPairs.get(cachedPairs.size() - 1);
	    QuestionAnswerPair lastNewPair = questionAnswerPairs.get(questionAnswerPairs.size() - 1);
	    
	    if ( !lastNewPair.equals(lastCachedPair) ) {
		// this is the case when the pandora service doesn't send back all the dialogue, thus the tricky part
		int mergingIndex = cachedPairs.indexOf(lastNewPair);
		if ( mergingIndex > -1 ) {
		    List<QuestionAnswerPair> truncatedPairs = cachedPairs.subList(mergingIndex + 1, cachedPairs.size());
//		    questionAnswerPairs.remove(questionAnswerPairs.size() - 1);
		    questionAnswerPairs.addAll(truncatedPairs);
		    System.err.println(truncatedPairs);
		}
	    }
	} 
	
	// either there are no previously cached pairs or the new pairs are merged with the diffed cached ones
	cachedResponsesByUserId.put(userId, questionAnswerPairs);
    }
}
