package com.epam.model.dto;

import com.epam.model.dao.XlsMapping;

public class AdminDTO {

    private String testType;
    private String login;
    private String pass;
    private String field;

    public String getTestType() {
        return testType;
    }

    @XlsMapping(header = "testType")
    public void setTestType(String testType) {
        this.testType = testType;
    }

    public String getLogin() {
        return login;
    }

    @XlsMapping(header = "login")
    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    @XlsMapping(header = "pass")
    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getField() {
        return field;
    }

    @XlsMapping(header = "field")
    public void setField(String field) {
        this.field = field;
    }

}
