package com.epam.business;

import com.epam.model.dto.CatalogueManagementDTO;
import com.epam.pages.adidas.LoginPage;
import com.epam.pages.adidas.WelcomePage;
import com.epam.pages.adidas.catalogue.CatalogueManagementPage;

import java.time.format.DateTimeFormatter;

public class BaseBO {

    protected DateTimeFormatter formatter;

    public WelcomePage login(String login, String password) {
        LoginPage loginPage = new LoginPage();
        return loginPage.login(login, password);
    }

    public CatalogueManagementPage openCatalogueManagementPage(CatalogueManagementDTO managementDTO) {
        WelcomePage welcomePage = login(managementDTO.getLogin(), managementDTO.getPass());
        return welcomePage.openCatalogueManagementModule();
    }
}
