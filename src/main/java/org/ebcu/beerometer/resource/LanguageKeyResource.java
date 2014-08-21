package org.ebcu.beerometer.resource;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("/api/v1/languagekeys")
public interface LanguageKeyResource {

    @GET
    @Produces("application/json")
    public Response getLanguageKeys(@QueryParam("lang") String languageCode);

    @GET
    @Path("/reload")
    @Produces("application/json")
    public String reloadDB();
}
