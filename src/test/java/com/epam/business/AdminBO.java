package com.epam.business;

import com.epam.pages.adidas.LoginPage;
import com.epam.pages.adidas.NewsPanelsPage;
import com.epam.pages.adidas.WelcomePage;
import com.epam.model.dto.AdminDTO;
import com.epam.pages.adidas.productfeeds.SeasonsPage;

public class AdminBO {

    public boolean validateLoginField(AdminDTO adminDTO) {
        LoginPage loginPage = new LoginPage();
        WelcomePage welcomePage = loginPage.login(adminDTO.getLogin(), adminDTO.getPass());
        return welcomePage.exist();
    }

    public boolean isNewsPanelsModuleOpen() {
        WelcomePage mainPage = new WelcomePage();
        NewsPanelsPage newsPanelsPage = mainPage.openNewsPanelsModule();
        newsPanelsPage.printTableRowsInfo();
        return newsPanelsPage.exist();
    }

    public boolean isSeasonsModuleOpen() {
        WelcomePage welcomePage = new WelcomePage();
        SeasonsPage seasonsPage = welcomePage.openProductFeedsModuleSeasons();
        return seasonsPage.exist();
    }
}
