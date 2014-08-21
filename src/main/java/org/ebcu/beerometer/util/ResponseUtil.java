package org.ebcu.beerometer.util;

import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

public class ResponseUtil {

    private static final int CACHE_MAX_AGE = 86400;

    public static Response addCacheControl(Object result) {
        CacheControl cc = new CacheControl();
        cc.setMaxAge(CACHE_MAX_AGE);
        cc.setPrivate(true);

        ResponseBuilder builder = Response.ok(result);
        builder.cacheControl(cc);
        return builder.build();
    }
}
