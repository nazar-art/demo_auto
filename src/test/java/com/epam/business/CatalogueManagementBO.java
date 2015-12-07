package com.epam.business;

import com.epam.model.dto.CatalogueManagementDTO;
import com.epam.pages.adidas.LoginPage;
import com.epam.pages.adidas.WelcomePage;
import com.epam.pages.adidas.catalogue.CatalogueManagementPage;

public class CatalogueManagementBO {

    public boolean isCatalogueManagementPageOpen(CatalogueManagementDTO managementDTO) {
        LoginPage loginPage = new LoginPage();
        WelcomePage welcomePage = loginPage.login(managementDTO.getLogin(), managementDTO.getPass());
        CatalogueManagementPage managementPage = welcomePage.openCatalogueManagementModule();
        return managementPage.exist();
    }


}
