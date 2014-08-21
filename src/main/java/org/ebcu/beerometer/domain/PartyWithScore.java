package org.ebcu.beerometer.domain;

public class PartyWithScore extends Party implements Comparable<PartyWithScore> {

    private final int DEF_SCORE = 50;

    private int       score     = DEF_SCORE;

    public PartyWithScore(Party party) {
        super(party.getId(), party.getCountryId(), party.getName());
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public int compareTo(PartyWithScore party) {
        return (party.getScore() - score);
    }
}
