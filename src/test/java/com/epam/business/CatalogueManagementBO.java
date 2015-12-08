package com.epam.business;

import com.epam.core.logging.Logger;
import com.epam.model.dto.CatalogueManagementDTO;
import com.epam.pages.adidas.catalogue.CatalogueManagementPage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CatalogueManagementBO extends BaseBO {

    public boolean isCatalogueManagementPageOpen(CatalogueManagementDTO managementDTO) {
        CatalogueManagementPage managementPage = openCatalogueManagementPage(managementDTO);
        return managementPage.exist();
    }

    public boolean isNewCatalogueAddedCanceled(CatalogueManagementDTO managementDTO) {
        CatalogueManagementPage managementPage = openCatalogueManagementPage(managementDTO);

        LocalDate today = LocalDate.now();
        LocalDate introDate = today.plusDays(4);
        LocalDate exitDate = today.plusDays(10);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedIntroDate = formatter.format(introDate);
        String formattedExitDate = formatter.format(exitDate);

        Logger.logInfo("Intro date: " + formattedIntroDate);
        Logger.logInfo("Exit date: " + formattedExitDate);

        return managementPage.clickNewCatalogueBtn()
                .fillNewCatalogueForm(managementDTO, formattedIntroDate, formattedExitDate)
                .clickCancelButton()
                .verifyThatCatalogueIsNotCreated(managementDTO.getShortName());
    }

}
