package org.ebcu.beerometer.domain;

public class Answer {
    private String candidateId;
    private String questionId;
    private int    point;
    private String comment;

    public Answer(String candidateId, String questionId, int point, String comment) {
        this.candidateId = candidateId;
        this.questionId = questionId;
        this.point = point;
        this.comment = comment;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
