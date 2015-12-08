package com.epam.business;

import com.epam.model.dto.CatalogueManagementDTO;
import com.epam.pages.adidas.LoginPage;
import com.epam.pages.adidas.WelcomePage;
import com.epam.pages.adidas.catalogue.CatalogueManagementPage;

public class BaseBO {

    protected WelcomePage login(CatalogueManagementDTO managementDTO) {
        LoginPage loginPage = new LoginPage();
        return loginPage.login(managementDTO.getLogin(), managementDTO.getPass());
    }

    protected CatalogueManagementPage openCatalogueManagementPage(CatalogueManagementDTO managementDTO) {
        WelcomePage welcomePage = login(managementDTO);
        return welcomePage.openCatalogueManagementModule();
    }
}
