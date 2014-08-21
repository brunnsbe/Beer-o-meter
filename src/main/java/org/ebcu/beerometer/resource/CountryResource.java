package org.ebcu.beerometer.resource;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("/api/v1/countries")
public interface CountryResource {

    @GET
    @Produces("application/json")
    public Response getCountries();

    @GET
    @Path("{id}")
    @Produces("application/json")
    public Response getCountry(@PathParam("id") String id);

    @GET
    @Path("/reload")
    @Produces("application/json")
    public String reloadDB();
}
