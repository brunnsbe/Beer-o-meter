package org.ebcu.beerometer.domain;

public class Candidate {

    private static final String URL_TWITTER = "https://twitter.com/";

    private String              id;
    private String              title;
    private String              lastName;
    private String              firstName;

    private String              countryId;
    private String              partyId;
    private String              districtId;

    private Integer             number;

    private String              description;
    private String              facebook;
    private String              twitter;
    private String              homepage;

    public Candidate() {}

    public Candidate(String id, String title, String lastName, String firstName, String partyId, String districtId,
            String countryId, Integer number, String description, String facebook, String twitter, String homepage) {

        this.id = id;
        this.title = title;
        this.lastName = lastName;
        this.firstName = firstName;
        this.partyId = partyId;
        this.districtId = districtId;
        this.countryId = countryId;
        this.number = number;
        this.description = description;
        this.facebook = facebook;
        this.twitter = twitter;
        this.homepage = homepage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        if (twitter != null && twitter.indexOf('@') == 0) {
            return URL_TWITTER + twitter.substring(1);
        } else {
            return twitter;
        }
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }
}
