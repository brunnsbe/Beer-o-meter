package org.ebcu.beerometer.services;

import java.util.*;
import java.util.concurrent.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.ebcu.beerometer.database.DBConnection;
import org.ebcu.beerometer.domain.*;
import org.ebcu.beerometer.resource.*;
import org.ebcu.beerometer.util.*;
import org.sql2o.Connection;

public class CandidateResourceService implements CandidateResource {

    private static final int                 DEF_POINTS             = 50;

    private static final String              QUERY_PARAM_COUNTRYID  = "CountryId";
    private static final String              QUERY_PARAM_PARTYID    = "PartyId";
    private static final String              QUERY_PARAM_DISTRICTID = "DistrictId";

    private static final String              SQL_LOAD_CANDIDATES    = "SELECT "
                                                                            + "Election_Candidate.Id, "
                                                                            + "Election_Candidate.Title, "
                                                                            + "Election_Candidate.LastName, "
                                                                            + "Election_Candidate.FirstName, "
                                                                            + "Election_Candidate.PartyId, "
                                                                            + "Election_Candidate.DistrictId, "
                                                                            + "Election_Party.CountryId, "
                                                                            + "Election_Candidate.Number, "
                                                                            + "Election_Candidate.Description, "
                                                                            + "Election_Candidate.Facebook, "
                                                                            + "Election_Candidate.Twitter, "
                                                                            + "Election_Candidate.Homepage "
                                                                            + "FROM "
                                                                            + "Election_Candidate "
                                                                            + "INNER JOIN Election_Party ON Election_Candidate.PartyId = Election_Party.Id "
                                                                            + "WHERE "
                                                                            + "EXISTS (SELECT 1 FROM Election_Answer WHERE Election_Answer.CandidateId = Election_Candidate.Id); ";

    private static final String              SQL_LOAD_ANSWERS       = "SELECT "
                                                                            + "Election_Candidate.Id AS CandidateId, "
                                                                            + "Election_Question.Id AS QuestionId, "
                                                                            + "ISNULL(Election_Answer.Point, "
                                                                            + DEF_POINTS
                                                                            + ") AS Point, "
                                                                            + "Election_Answer.Comment "
                                                                            + "FROM "
                                                                            + "Election_Candidate "
                                                                            + "CROSS JOIN Election_Question "
                                                                            + "LEFT OUTER JOIN Election_Answer ON Election_Candidate.Id = Election_Answer.CandidateId "
                                                                            + "AND Election_Answer.QuestionId = Election_Question.Id "
                                                                            + "WHERE "
                                                                            + "EXISTS (SELECT 1 FROM Election_Answer WHERE Election_Answer.CandidateId = Election_Candidate.Id) "
                                                                            + "ORDER BY "
                                                                            + "Election_Candidate.Id, "
                                                                            + "Election_Question.OrderNumber;";

    private static Map<String, Candidate>    candidates             = new ConcurrentHashMap<String, Candidate>();
    private static Map<String, List<Answer>> answers                = new ConcurrentHashMap<String, List<Answer>>();

    static {
        loadDB();
    }

    public static Map<String, Candidate> getCandidates() {
        return candidates;
    }

    public static Map<String, List<Answer>> getAnswers() {
        return answers;
    }

    private static void loadDB() {
        loadCandidatesFromDB();
        loadAnswersFromDB();
    }

    @Override
    public String reloadDB() {
        loadDB();
        return new String();
    }

    @Override
    public Response getCandidates(UriInfo info) {
        String countryId = info.getQueryParameters().getFirst(QUERY_PARAM_COUNTRYID);
        String partyId = info.getQueryParameters().getFirst(QUERY_PARAM_PARTYID);
        String districtId = info.getQueryParameters().getFirst(QUERY_PARAM_DISTRICTID);

        List<CandidateWithScore> result = new ArrayList<CandidateWithScore>();
        for (Candidate candidate : candidates.values()) {

            if (countryId != null && !countryId.equals(candidate.getCountryId())) {
                continue;
            }

            if (partyId != null && !partyId.equals(candidate.getPartyId())) {
                continue;
            }

            if (districtId != null && !districtId.equals(candidate.getDistrictId())) {
                continue;
            }
            CandidateWithScore candidateWithScore = new CandidateWithScore(candidate);

            calculateScore(candidateWithScore, info);

            result.add(candidateWithScore);
        }

        Collections.sort(result);

        return ResponseUtil.addCacheControl(result);
    }

    private static void calculateScore(CandidateWithScore candidate, UriInfo info) {
        List<Answer> candidateAnswers = answers.get(candidate.getId());
        if (candidateAnswers == null) {
            return;
        }

        double sum = 0;
        int counter = 0;

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

        candidate.setScore((int)Math.round(sum / counter));
    }

    @Override
    public Response getCandidate(String id) {
        Candidate candidate = candidates.get(id);
        if (candidate != null) {
            CandidateWithAnswer candidateWithAnswer = new CandidateWithAnswer(candidate);
            candidateWithAnswer.setAnswers(answers.get(candidateWithAnswer.getId()));
            return ResponseUtil.addCacheControl(candidateWithAnswer);
        } else {
            throw new NotFoundException();
        }
    }

    private static void loadCandidatesFromDB() {
        List<Candidate> candidatesSet;
        try (Connection con = DBConnection.getConnection().open()) {
            candidatesSet = con.createQuery(SQL_LOAD_CANDIDATES).executeAndFetch(Candidate.class);
            for (Candidate candidate : candidatesSet) {
                candidates.put(candidate.getId(), candidate);
            }
        }
    }

    private static void loadAnswersFromDB() {
        List<Answer> answersSet;
        try (Connection con = DBConnection.getConnection().open()) {
            answersSet = con.createQuery(SQL_LOAD_ANSWERS).executeAndFetch(Answer.class);
            for (Answer answer : answersSet) {
                if (candidates.containsKey(answer.getCandidateId())) {
                    List<Answer> candidateAnswers = answers.get(answer.getCandidateId());
                    if (candidateAnswers == null) {
                        candidateAnswers = new ArrayList<Answer>();
                        answers.put(answer.getCandidateId(), candidateAnswers);
                    }
                    candidateAnswers.add(answer);
                }
            }
        }
    }
}
