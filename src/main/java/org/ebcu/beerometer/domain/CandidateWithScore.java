package org.ebcu.beerometer.domain;

public class CandidateWithScore extends Candidate implements Comparable<CandidateWithScore> {

    private final int DEF_SCORE = 50;

    private int       score     = DEF_SCORE;

    public CandidateWithScore(Candidate candidate) {
        super(candidate.getId(), candidate.getTitle(), candidate.getLastName(), candidate.getFirstName(),
                candidate.getPartyId(), candidate.getDistrictId(), candidate.getCountryId(), candidate.getNumber(),
                candidate.getDescription(), candidate.getFacebook(), candidate.getTwitter(), candidate.getHomepage());
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public int compareTo(CandidateWithScore candidate) {
        return (candidate.getScore() - score);
    }
}
