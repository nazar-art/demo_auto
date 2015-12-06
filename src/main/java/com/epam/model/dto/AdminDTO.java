package com.epam.model.dto;

import com.epam.model.dao.XlsMapping;
import lombok.Data;

@Data
public class AdminDTO {

    @XlsMapping(header = "testType")
    private String testType;

    @XlsMapping(header = "login")
    private String login;

    @XlsMapping(header = "pass")
    private String pass;

}
