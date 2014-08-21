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

public class PartyResourceService implements PartyResource {

    private static final String       QUERY_PARAM_COUNTRYID  = "CountryId";
    private static final String       QUERY_PARAM_DISTRICTID = "DistrictId";

    private static final String       SQL_LOAD_PARTIES       = "SELECT "
                                                                     + "Election_Party.Id, "
                                                                     + "Election_Party.CountryId, "
                                                                     + "Election_Party.Name "
                                                                     + "FROM "
                                                                     + "Election_Party "
                                                                     + "WHERE "
                                                                     + "EXISTS (SELECT 1 FROM Election_Answer INNER JOIN Election_Candidate ON "
                                                                     + "Election_Answer.CandidateId = Election_Candidate.Id"
                                                                     + " WHERE Election_Candidate.PartyId = Election_Party.Id);";

    private static Map<String, Party> parties                = new ConcurrentHashMap<String, Party>();

    static {
        loadPartiesFromDB();
    }

    @Override
    public String reloadDB() {
        loadPartiesFromDB();
        return new String();
    }

    @Override
    public Response getParties(UriInfo info) {
        String countryId = info.getQueryParameters().getFirst(QUERY_PARAM_COUNTRYID);

        List<PartyWithScore> result = new ArrayList<PartyWithScore>();
        for (Party party : parties.values()) {

            if (countryId != null && !countryId.equals(party.getCountryId())) {
                continue;
            }

            PartyWithScore partyWithScore = new PartyWithScore(party);

            calculateScore(partyWithScore, info);

            result.add(partyWithScore);
        }

        Collections.sort(result);

        return ResponseUtil.addCacheControl(result);
    }

    private static void calculateScore(PartyWithScore partyWithScore, UriInfo info) {
        String districtId = info.getQueryParameters().getFirst(QUERY_PARAM_DISTRICTID);

        double sum = 0;
        int counter = 0;

        for (Candidate candidate : CandidateResourceService.getCandidates().values()) {
            if (!partyWithScore.getId().equals(candidate.getPartyId())) {
                continue;
            }
            if (districtId != null && !districtId.equals(candidate.getDistrictId())) {
                continue;
            }

            List<Answer> candidateAnswers = CandidateResourceService.getAnswers().get(candidate.getId());
            if (candidateAnswers == null) {
                continue;
            }

            for (Answer answer : candidateAnswers) {
                String questionId = answer.getQuestionId();

                String sUserPoint = info.getQueryParameters().getFirst(questionId);
                if (sUserPoint != null) {
                    try {
                        int userPoint = Integer.parseInt(sUserPoint);
                        sum += 100 - Math.abs((answer.getPoint() - userPoint));
                        counter++;
                    } catch (NumberFormatException e) {}
                }
            }
            partyWithScore.setScore((int)Math.round(sum / counter));
        }
    }

    @Override
    public Response getParty(String id) {
        Party party = parties.get(id);
        if (party != null) {
            return ResponseUtil.addCacheControl(party);
        } else {
            throw new NotFoundException();
        }
    }

    private static void loadPartiesFromDB() {

        List<Party> partiesSet;
        try (Connection con = DBConnection.getConnection().open()) {
            partiesSet = con.createQuery(SQL_LOAD_PARTIES).executeAndFetch(Party.class);
            for (Party party : partiesSet) {
                parties.put(party.getId(), party);
            }
        }
    }
}
