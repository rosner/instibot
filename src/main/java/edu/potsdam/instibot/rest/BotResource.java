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
import edu.potsdam.instibot.bot.UserCredentials;

@Component
@Path("bot/respond")
public class BotResource {

     @Resource
    protected PandoraBotsWrapper pandoraBotsWrapper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserCredentials(String botId) {
	UserCredentials userCredentials = pandoraBotsWrapper.getNewUserCredentials(botId);
	if (userCredentials == null) {
	    throw new WebApplicationException(Response.status(Status.SERVICE_UNAVAILABLE).build());
	}
	return Response.status(Status.OK).
    }
    
    @GET
//    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("{user_id}/{user_input}")
    public String respond(@PathParam("user_id") String userId, @PathParam("user_input") String userInput) {
	if (userId.isEmpty()) {
	    return "Hello new User";
	} else {
	    
	 return "Hello old User";   
	}
    }
    /*
     * @GET
     * 
     * @Produces("application/xml") public Addresses getAllAddresses() {
     * Addresses addresses = new Addresses();
     * addresses.getKeys().addAll(addressService.getAddressKeys()); return
     * addresses; }
     * 
     * @GET
     * 
     * @Path("{key}")
     * 
     * @Produces("application/xml") public Address getAddress(@PathParam("key")
     * String key) { return addressService.findAddressByKey(key); }
     * 
     * @DELETE
     * 
     * @Path("{key}") public void deleteAddress(@PathParam("key") String key) {
     * addressService.deleteAddressByKey(key); }
     * 
     * @PUT
     * 
     * @Consumes("application/xml") public void updateAddress(Address address) {
     * addressService.insertAddress(address); }
     * 
     * @POST
     * 
     * @Consumes("application/xml") public void insertNewAddress(Address
     * address) { addressService.insertAddress(address); }
     */
}
