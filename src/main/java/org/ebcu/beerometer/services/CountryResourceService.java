package org.ebcu.beerometer.services;

import java.util.*;
import java.util.concurrent.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.ebcu.beerometer.domain.*;
import org.ebcu.beerometer.database.*;
import org.ebcu.beerometer.resource.*;
import org.ebcu.beerometer.util.*;
import org.sql2o.Connection;

public class CountryResourceService implements CountryResource {

    private static final String         SQL_LOAD_COUNTRIES = "SELECT "
                                                                   + "Election_Country.Id, "
                                                                   + "Election_Country.Name "
                                                                   + "FROM "
                                                                   + "Election_Country "
                                                                   + "WHERE "
                                                                   + "EXISTS (SELECT 1 FROM Election_Answer "
                                                                   + " INNER JOIN Election_Candidate ON Election_Answer.CandidateId = Election_Candidate.Id "
                                                                   + " INNER JOIN Election_Party ON Election_Candidate.PartyId = Election_Party.Id "
                                                                   + " WHERE Election_Party.CountryId = Election_Country.Id);";

    private static Map<String, Country> countries          = new ConcurrentHashMap<String, Country>();

    static {
        loadCountriesFromDB();
    }

    @Override
    public String reloadDB() {
        loadCountriesFromDB();
        return new String();
    }

    @Override
    public Response getCountries() {
        List<Country> result = new ArrayList<Country>(countries.values());
        return ResponseUtil.addCacheControl(result);
    }

    @Override
    public Response getCountry(String id) {
        Country country = countries.get(id);
        if (country != null) {
            return ResponseUtil.addCacheControl(country);
        } else {
            throw new NotFoundException();
        }
    }

    private static void loadCountriesFromDB() {
        List<Country> countrySet;
        try (Connection con = DBConnection.getConnection().open()) {
            countrySet = con.createQuery(SQL_LOAD_COUNTRIES).executeAndFetch(Country.class);
            for (Country country : countrySet) {
                countries.put(country.getId(), country);
            }
        }
    }
}
