package edu.potsdam.instibot.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.stereotype.Component;

@Component
@Path("/ask_the_bot")
public class BotRequest {
    
//    @Resource
//    private AddressService addressService;
    
    @GET
    @Produces("application/xml")
    public String getHelloWorld() {
	return "Hello World";
    }
    /*
    @GET
    @Produces("application/xml")
    public Addresses getAllAddresses() {
      Addresses addresses = new Addresses();
      addresses.getKeys().addAll(addressService.getAddressKeys());
      return addresses;
    }

    @GET
    @Path("{key}")
    @Produces("application/xml")
    public Address getAddress(@PathParam("key") String key) {
      return addressService.findAddressByKey(key);
    }

    @DELETE
    @Path("{key}")
    public void deleteAddress(@PathParam("key") String key) {
      addressService.deleteAddressByKey(key);
    }

    @PUT
    @Consumes("application/xml")
    public void updateAddress(Address address) {
      addressService.insertAddress(address);
    }

    @POST
    @Consumes("application/xml")
    public void insertNewAddress(Address address) {
      addressService.insertAddress(address);
    }
*/
}
