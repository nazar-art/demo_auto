package com.epam.model.dto;

import com.epam.core.annotations.InjectRandomData;
import com.epam.core.datafactory.RandomType;
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

    @XlsMapping(header = "description")
    private String description;


    // check random generation
    @InjectRandomData(type = RandomType.EMAIL)
    private String email;
}
