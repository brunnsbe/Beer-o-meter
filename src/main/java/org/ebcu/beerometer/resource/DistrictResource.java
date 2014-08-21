package org.ebcu.beerometer.resource;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("/api/v1/districts")
public interface DistrictResource {

    @GET
    @Produces("application/json")
    public Response getDistricts(@Context UriInfo info);

    @GET
    @Path("{id}")
    @Produces("application/json")
    public Response getDistrict(@PathParam("id") String id);

    @GET
    @Path("/reload")
    @Produces("application/json")
    public String reloadDB();
}
