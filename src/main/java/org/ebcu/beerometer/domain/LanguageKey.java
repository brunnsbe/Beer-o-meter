package org.ebcu.beerometer.domain;

public class LanguageKey {
    private String code;
    private String name;
    private String data;

    public LanguageKey(String code, String name, String data) {
        this.code = code;
        this.name = name;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
