package com.epam.model.dto;

import com.epam.model.dao.XlsMapping;
import lombok.Data;

@Data
public class CatalogueManagementDTO {

    @XlsMapping(header = "testType")
    private String testType;

    @XlsMapping(header = "login")
    private String login;

    @XlsMapping(header = "pass")
    private String pass;

    @XlsMapping(header = "shortName")
    private String shortName;

    @XlsMapping(header = "longName")
    private String longName;

    @XlsMapping(header = "configurationSet")
    private String configurationSet;

//    @GenerateData(type = RandomType.WORD, min = 6, max = 12)
    @XlsMapping(header = "description")
    private String description;

}
