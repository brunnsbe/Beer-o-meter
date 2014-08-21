package org.ebcu.beerometer.resource;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("/api/v1/candidates")
public interface CandidateResource {

    @GET
    @Produces("application/json")
    public Response getCandidates(@Context UriInfo info);

    @GET
    @Path("{id}")
    @Produces("application/json")
    public Response getCandidate(@PathParam("id") String id);

    @GET
    @Path("/reload")
    @Produces("application/json")
    public String reloadDB();
}
