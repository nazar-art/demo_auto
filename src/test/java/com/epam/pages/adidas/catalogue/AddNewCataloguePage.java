package com.epam.pages.adidas.catalogue;

import com.epam.core.annotations.Page;
import com.epam.core.components.AbstractPageElement;
import com.epam.core.components.ElementUtils;
import com.epam.core.components.WebFieldDecorator;
import com.epam.core.components.element.Button;
import com.epam.core.components.element.Selector;
import com.epam.core.components.element.TextInput;
import com.epam.core.logging.Logger;
import com.epam.model.dto.CatalogueManagementDTO;
import com.epam.pages.PageObject;
import com.epam.pages.adidas.WelcomePage;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;

@Page(title = "AddNewCataloguePage")
public class AddNewCataloguePage extends WelcomePage {

    public AddNewCataloguePage() {
        successor = new HashMap<Class<? extends PageObject>, Button>();
        PageFactory.initElements(new WebFieldDecorator(getDriver()), this);
    }

    @Override
    public boolean exist() {
        return inputShortName.isPresent()
                && inputLongName.isPresent()
                && inputDescription.isPresent();
    }


    @FindBy(id = "name")
    private TextInput inputShortName;

    @FindBy(id = "longName")
    private TextInput inputLongName;

    @FindBy(id = "introDate")
    private TextInput inputIntroDate;

    @FindBy(id = "expDate")
    private TextInput inputExitDate;



    @FindBy(xpath = "id('catalogDetails')//th[@class='next']")
    private Button btnNextMonth;

    // todo configure this element like submenu with list of items
    @FindBy(css = ".dropdown-toggle.cms-configuration-toggle")
    private Selector selectConfigurationSet;

    @FindBy(id = "description")
    private TextInput inputDescription;

    @FindBy(xpath = "id('catalogueDetailsForm')//a[contains(text(), 'Cancel')]")
    private Button btnCacel;

    @FindBy(xpath = "id('catalogueDetailsForm')//button")
    private Button btnSave;


    public AddNewCataloguePage fillNewCatalogueForm(CatalogueManagementDTO managementDTO, String introDate, String exitDate) {
        setShortName(managementDTO);
        setLongName(managementDTO);

        ElementUtils.setDate(inputIntroDate, introDate);
        ElementUtils.setDate(inputExitDate, exitDate);

        setConfigurationSet(managementDTO);
        setDescription(managementDTO);
        return this;
    }

    public AddNewCataloguePage setDescription(CatalogueManagementDTO managementDTO) {
        inputDescription.sendText(managementDTO.getDescription());
        return this;
    }

    public AddNewCataloguePage setConfigurationSet(CatalogueManagementDTO managementDTO) {
        selectConfigurationSet.deselectByVisibleText(managementDTO.getConfigurationSet());
        return this;
    }

    public AddNewCataloguePage setLongName(CatalogueManagementDTO managementDTO) {
        inputLongName.sendText(managementDTO.getLongName());
        return this;
    }

    public AddNewCataloguePage setShortName(CatalogueManagementDTO managementDTO) {
        inputShortName.sendText(managementDTO.getShortName());
        return this;
    }

    public CatalogueManagementPage clickCancelButton() {
        btnCacel.click();
        return new CatalogueManagementPage();
    }

    public CatalogueManagementPage clickSaveButton() {
        btnSave.click();
        return new CatalogueManagementPage();
    }


    @Deprecated
    private void setDate(LocalDate introDate) {
        String month = introDate.getMonth().getDisplayName(TextStyle.FULL, Locale.UK);
        Logger.logInfo("MONTH: " + month);
        if (AbstractPageElement.getFromCalendar(month, "1") == null
                && AbstractPageElement.getFromCalendar(month, "2") == null) {
            if (btnNextMonth.visibilityOfElementWait()) {
                btnNextMonth.click();
                Logger.logInfo("next month click ");
            }
        }
        // dd.mm.yyyy
        Logger.logInfo("Point 1 remove");

        if (setCalendarDay(introDate)) {
            Logger.logInfo("Day is set to: " + introDate);
        } else {
            btnNextMonth.click();

            if (setCalendarDay(introDate)) {
                Logger.logInfo("Day is set to: " + introDate);
            }
            Logger.logWarning("Day is not set to: " + introDate);
        }
    }

    @Deprecated
    private boolean setCalendarDay(LocalDate date) { // 2015-09-01
        int dayOfMonth = date.getDayOfMonth();
        Logger.logDebug("DAY OF MONTH IS: " + dayOfMonth);
        String day = String.valueOf(dayOfMonth);
        return AbstractPageElement.clickIfExistInCalendar(day);
    }

}
