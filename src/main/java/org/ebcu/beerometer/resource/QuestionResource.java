package org.ebcu.beerometer.resource;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("/api/v1/questions")
public interface QuestionResource {

    @GET
    @Produces("application/json")
    public Response getQuestions();

    @GET
    @Path("{id}")
    @Produces("application/json")
    public Response getQuestion(@PathParam("id") String id);

    @GET
    @Path("/reload")
    @Produces("application/json")
    public String reloadDB();
}
