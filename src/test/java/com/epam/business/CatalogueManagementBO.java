package com.epam.business;

import com.epam.core.logging.Logger;
import com.epam.core.utils.CalendarUtils;
import com.epam.model.dto.CatalogueManagementDTO;
import com.epam.pages.adidas.catalogue.CatalogueManagementPage;
import com.epam.pages.adidas.catalogue.CatalogueViewPage;

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

        boolean catalogCreationResult = new CatalogueManagementPage().clickNewCatalogueBtn()
                .fillNewCatalogueForm(managementDTO, formattedIntroDate, formattedExitDate)
                .clickSaveButton()
                .isCatalogReallyNew();

        return catalogCreationResult
                && new CatalogueViewPage()
                .returnToCataloguesPage()
                .verifyThatCatalogueIsCreated(managementDTO.getShortName());

    }

    public CatalogueManagementPage deleteCatalogue(String catalogueName) {
        CatalogueManagementPage managementPage = new CatalogueManagementPage();
        return managementPage
                .viewCatalogueDetails(catalogueName)
                .clickDeleteButton()
                .clickConfirmDeletion();
    }

    public boolean isNewCatalogueDeleted(CatalogueManagementDTO dto) {
        openCatalogueManagementPage(dto);

        String catalogueName = dto.getShortName();
        boolean result = false;

        if (isNewCatalogueAdded(dto)) {
            CatalogueManagementPage managementPage = deleteCatalogue(catalogueName);
            result = managementPage.verifyThatCatalogueIsNotCreated(dto.getShortName());
        }

        return result;
    }
}
