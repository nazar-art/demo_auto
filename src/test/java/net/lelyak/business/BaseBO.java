package net.lelyak.business;

import net.lelyak.model.dto.CatalogueManagementDTO;
import net.lelyak.pages.abibas.LoginPage;
import net.lelyak.pages.abibas.WelcomePage;
import net.lelyak.pages.abibas.catalogue.CatalogueManagementPage;

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
