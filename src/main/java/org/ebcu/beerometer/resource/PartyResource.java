package org.ebcu.beerometer.resource;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("/api/v1/parties")
public interface PartyResource {

    @GET
    @Produces("application/json")
    public Response getParties(@Context UriInfo info);

    @GET
    @Path("{id}")
    @Produces("application/json")
    public Response getParty(@PathParam("id") String id);

    @GET
    @Path("/reload")
    @Produces("application/json")
    public String reloadDB();
}
