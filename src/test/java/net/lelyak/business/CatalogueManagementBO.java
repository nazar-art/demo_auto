package net.lelyak.business;

import net.lelyak.core.logging.Logger;
import net.lelyak.core.utils.CalendarUtils;
import net.lelyak.model.dto.CatalogueManagementDTO;
import net.lelyak.pages.abibas.catalogue.CatalogueManagementPage;
import net.lelyak.pages.abibas.catalogue.CatalogueViewPage;
import org.apache.commons.lang.RandomStringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CatalogueManagementBO extends BaseBO {

    public static final int INTRO_DATE_DAYS_DIFFERENCE = 4;
    public static final int EXIT_DATE_DAYS_DIFFERENCE = 10;
    public static final String SEPARATOR = "_";
    public static final int UPDATE_INDEX = 3;

    public boolean isCatalogueManagementPageOpen(CatalogueManagementDTO managementDTO) {
        CatalogueManagementPage managementPage = openCatalogueManagementPage(managementDTO);
        return managementPage.exist();
    }

    public boolean isNewCatalogueCanceled(CatalogueManagementDTO dto) {
        CatalogueManagementPage managementPage = openCatalogueManagementPage(dto);

        makeNamesUnique(UPDATE_INDEX, dto);

        LocalDate today = LocalDate.now();
        LocalDate introDate = today.plusDays(4);
        LocalDate exitDate = today.plusDays(10);

        formatter = DateTimeFormatter.ofPattern(CalendarUtils.DATE_FORMAT);
        String formattedIntroDate = formatter.format(introDate);
        String formattedExitDate = formatter.format(exitDate);

        Logger.logInfo("Intro date: " + formattedIntroDate);
        Logger.logInfo("Exit date: " + formattedExitDate);

        return managementPage
                .clickNewCatalogueBtn()
                .fillNewCatalogueForm(dto.getShortName(), dto.getLongName(),
                        dto.getConfigurationSet(), dto.getDescription(),
                        formattedExitDate, formattedIntroDate)
                .clickCancelButton()
                .verifyThatCatalogueIsNotCreated(dto.getShortName());
    }

    private void makeNamesUnique(int uniqueIndex, CatalogueManagementDTO dto) {
        String value = RandomStringUtils.randomNumeric(uniqueIndex);

        String updatedShortName = dto.getShortName() + SEPARATOR + value;
        String updatedLongName = dto.getLongName() + SEPARATOR + value;

        dto.setShortName(updatedShortName);
        dto.setLongName(updatedLongName);
    }

    public boolean isNewCatalogueAdded(CatalogueManagementDTO dto) {
        makeNamesUnique(UPDATE_INDEX, dto);

        LocalDate today = LocalDate.now();
        LocalDate introDate = today.plusDays(INTRO_DATE_DAYS_DIFFERENCE);
        LocalDate exitDate = today.plusDays(EXIT_DATE_DAYS_DIFFERENCE);

        formatter = DateTimeFormatter.ofPattern(CalendarUtils.DATE_FORMAT);
        String formattedIntroDate = formatter.format(introDate);
        String formattedExitDate = formatter.format(exitDate);

        Logger.logInfo("Intro date: " + formattedIntroDate);
        Logger.logInfo("Exit date: " + formattedExitDate);

        boolean catalogCreationResult = new CatalogueManagementPage().clickNewCatalogueBtn()
                .fillNewCatalogueForm(dto.getShortName(), dto.getLongName(),
                        dto.getConfigurationSet(), dto.getDescription(),
                        formattedExitDate, formattedIntroDate)
                .clickSaveButton()
                .isCatalogReallyNew();

        return catalogCreationResult
                && new CatalogueViewPage()
                .returnToCataloguesPage()
                .verifyThatCatalogueIsCreated(dto.getShortName());

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
