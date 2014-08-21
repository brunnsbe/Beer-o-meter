package org.ebcu.beerometer;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

import org.ebcu.beerometer.services.*;

public class BeerOMeterApplication extends Application
{
    private Set<Object>   singletons = new HashSet<Object>();
    private Set<Class<?>> empty      = new HashSet<Class<?>>();

    public BeerOMeterApplication() {
        singletons.add(new CandidateResourceService());
        singletons.add(new PartyResourceService());
        singletons.add(new CountryResourceService());
        singletons.add(new DistrictResourceService());
        singletons.add(new QuestionResourceService());
        singletons.add(new LanguageKeyResourceService());
    }

    @Override
    public Set<Class<?>> getClasses() {
        return empty;
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }

}
