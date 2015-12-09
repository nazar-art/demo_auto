package com.epam.business;

import com.epam.core.logging.Logger;
import com.epam.core.utils.CalendarUtils;
import com.epam.model.dto.CatalogueManagementDTO;
import com.epam.pages.adidas.catalogue.CatalogueManagementPage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CatalogueManagementBO extends BaseBO {

    public static final int INTRO_DATE_DAYS_DIFFERENCE = 4;
    public static final int EXIT_DATE_DAYS_DIFFERENCE = 10;

    public boolean isCatalogueManagementPageOpen(CatalogueManagementDTO managementDTO) {
        CatalogueManagementPage managementPage = openCatalogueManagementPage(managementDTO);
        return managementPage.exist();
    }

    public boolean isNewCatalogueCanceled(CatalogueManagementDTO managementDTO) {
        CatalogueManagementPage managementPage = openCatalogueManagementPage(managementDTO);

        LocalDate today = LocalDate.now();
        LocalDate introDate = today.plusDays(4);
        LocalDate exitDate = today.plusDays(10);

        formatter = DateTimeFormatter.ofPattern(CalendarUtils.DATE_FORMAT);
        String formattedIntroDate = formatter.format(introDate);
        String formattedExitDate = formatter.format(exitDate);

        Logger.logInfo("Intro date: " + formattedIntroDate);
        Logger.logInfo("Exit date: " + formattedExitDate);

        return managementPage.clickNewCatalogueBtn()
                .fillNewCatalogueForm(managementDTO, formattedIntroDate, formattedExitDate)
                .clickCancelButton()
                .verifyThatCatalogueIsNotCreated(managementDTO.getShortName());
    }

    public boolean isNewCatalogueAdded(CatalogueManagementDTO managementDTO) {
        LocalDate today = LocalDate.now();
        LocalDate introDate = today.plusDays(INTRO_DATE_DAYS_DIFFERENCE);
        LocalDate exitDate = today.plusDays(EXIT_DATE_DAYS_DIFFERENCE);

        formatter = DateTimeFormatter.ofPattern(CalendarUtils.DATE_FORMAT);
        String formattedIntroDate = formatter.format(introDate);
        String formattedExitDate = formatter.format(exitDate);

        Logger.logInfo("Intro date: " + formattedIntroDate);
        Logger.logInfo("Exit date: " + formattedExitDate);

        return new CatalogueManagementPage().clickNewCatalogueBtn()
                .fillNewCatalogueForm(managementDTO, formattedIntroDate, formattedExitDate)
                .clickSaveButton()
                .returnToCataloguesPage()
                .verifyThatCatalogueIsCreated(managementDTO.getShortName());
    }
}
