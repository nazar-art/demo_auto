package com.epam.model.dto;

import com.epam.model.dao.XlsMapping;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class AdminDTO {

    private String testType;
    private String login;
    private String pass;

    @XlsMapping(header = "testType")
    public void setTestType(String testType) {
        this.testType = testType;
    }

    @XlsMapping(header = "login")
    public void setLogin(String login) {
        this.login = login;
    }

    @XlsMapping(header = "pass")
    public void setPass(String pass) {
        this.pass = pass;
    }
}
