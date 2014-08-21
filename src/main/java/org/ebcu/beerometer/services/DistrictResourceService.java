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

public class DistrictResourceService implements DistrictResource {

    private static final String          QUERY_PARAM_COUNTRYID = "CountryId";

    private static final String          SQL_LOAD_DISTRICTS    = "SELECT "
                                                                       + "Election_District.Id, "
                                                                       + "Election_District.CountryId, "
                                                                       + "Election_District.Name "
                                                                       + "FROM "
                                                                       + "Election_District "
                                                                       + "WHERE "
                                                                       + "EXISTS (SELECT 1 FROM Election_Answer "
                                                                       + " INNER JOIN Election_Candidate ON Election_Answer.CandidateId = Election_Candidate.Id "
                                                                       + " WHERE Election_Candidate.DistrictId = Election_District.Id);";

    private static Map<String, District> districts             = new ConcurrentHashMap<String, District>();

    static {
        loadDistrictsFromDB();
    }

    @Override
    public String reloadDB() {
        loadDistrictsFromDB();
        return new String();
    }

    @Override
    public Response getDistricts(UriInfo info) {
        String countryId = info.getQueryParameters().getFirst(QUERY_PARAM_COUNTRYID);

        List<District> result = new ArrayList<District>();
        for (District district : districts.values()) {
            if (countryId != null && !countryId.equals(district.getCountryId())) {
                continue;
            }
            result.add(district);
        }

        return ResponseUtil.addCacheControl(result);
    }

    @Override
    public Response getDistrict(String id) {
        District district = districts.get(id);
        if (district != null) {
            return ResponseUtil.addCacheControl(district);
        } else {
            throw new NotFoundException();
        }
    }

    private static void loadDistrictsFromDB() {
        List<District> districtSet;
        try (Connection con = DBConnection.getConnection().open()) {
            districtSet = con.createQuery(SQL_LOAD_DISTRICTS).executeAndFetch(District.class);
            for (District district : districtSet) {
                districts.put(district.getId(), district);
            }
        }
    }
}
