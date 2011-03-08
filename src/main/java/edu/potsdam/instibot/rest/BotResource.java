package edu.potsdam.instibot.rest;

import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.stereotype.Component;

import edu.potsdam.instibot.bot.PandoraBotsWrapper;

@Component
@Path("bot/")
public class BotResource {

    @Resource
    protected PandoraBotsWrapper pandoraBotsWrapper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("user_credentials/{bot_id}")
    public UserCredentials getUserCredentials(@PathParam("bot_id") String botId) {
	UserCredentials userCredentials = pandoraBotsWrapper.getNewUserCredentials(botId);
	if (userCredentials == null) {
	    throw new WebApplicationException(Response.status(Status.SERVICE_UNAVAILABLE).build());
	}
	return userCredentials;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("respond/{bot_id}/{user_id}/{user_input}")
    public String respond(
	    @PathParam("bot_id") String botId,
	    @PathParam("user_id") String userId, 
	    @PathParam("user_input") String userInput) {	
	BotResponse botResponse = pandoraBotsWrapper.getBotResponse(botId, userId, userInput);
	
	return "\"" + userInput +  "\"";
    }
}
