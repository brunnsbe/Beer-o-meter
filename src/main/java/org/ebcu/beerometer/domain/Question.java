package org.ebcu.beerometer.domain;

public class Question implements Comparable<Question> {
    private String  id;
    private String  subject;
    private Integer orderNumber;

    public Question(String id, String subject, Integer orderNumber) {
        this.id = id;
        this.subject = subject;
        this.orderNumber = orderNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void getOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Override
    public int compareTo(Question question) {
        return (orderNumber - question.getOrderNumber());
    }
}
