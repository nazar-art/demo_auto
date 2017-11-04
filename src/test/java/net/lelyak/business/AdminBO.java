package net.lelyak.business;

import net.lelyak.pages.abibas.LoginPage;
import net.lelyak.pages.abibas.NewsPanelsPage;
import net.lelyak.pages.abibas.WelcomePage;
import net.lelyak.model.dto.AdminDTO;
import net.lelyak.pages.abibas.productfeeds.SeasonsPage;

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
//        return false;
    }
}
