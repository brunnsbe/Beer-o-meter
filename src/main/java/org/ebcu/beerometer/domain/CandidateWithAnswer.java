package org.ebcu.beerometer.domain;

import java.util.*;

public class CandidateWithAnswer extends Candidate {

    private List<Answer> answers;

    public CandidateWithAnswer(Candidate candidate) {
        super(candidate.getId(), candidate.getTitle(), candidate.getLastName(), candidate.getFirstName(),
                candidate.getPartyId(), candidate.getDistrictId(), candidate.getCountryId(), candidate.getNumber(),
                candidate.getDescription(), candidate.getFacebook(), candidate.getTwitter(), candidate.getHomepage());
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}
