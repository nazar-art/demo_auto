package com.epam.pages.adidas.catalogue;

import com.epam.core.annotations.Page;
import com.epam.core.components.AbstractPageElement;
import com.epam.core.components.WebFieldDecorator;
import com.epam.core.components.element.Button;
import com.epam.core.components.element.TextInput;
import com.epam.core.driver.Driver;
import com.epam.core.logging.Logger;
import com.epam.core.utils.CalendarUtils;
import com.epam.core.utils.ElementUtils;
import com.epam.model.dto.CatalogueManagementDTO;
import com.epam.pages.PageObject;
import com.epam.pages.adidas.WelcomePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
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

    @FindBy(id = "description")
    private TextInput inputDescription;

    @FindBy(xpath = "id('catalogueDetailsForm')//a[contains(text(), 'Cancel')]")
    private Button btnCancel;

    @FindBy(xpath = "id('catalogueDetailsForm')//button")
    private Button btnSave;


    public AddNewCataloguePage fillNewCatalogueForm(CatalogueManagementDTO managementDTO, String introDate, String exitDate) {
        setShortName(managementDTO);
        setLongName(managementDTO);

        CalendarUtils.setDate(inputIntroDate, introDate);
        CalendarUtils.setDate(inputExitDate, exitDate);

        setConfigurationSet(managementDTO.getConfigurationSet());
        setDescription(managementDTO);
        return this;
    }

    public AddNewCataloguePage setDescription(CatalogueManagementDTO managementDTO) {
        inputDescription.sendText(managementDTO.getDescription());
        return this;
    }

    public AddNewCataloguePage setConfigurationSet(String configuration) {
        WebElement listParent = Driver.findByXpath("//div[@class='form-element-wrapper fix-for-qtip']");
        listParent.click();

        WebElement item = listParent.findElement(By.xpath("//a[contains(text(), '" + configuration + "')]"));
        ElementUtils.highlightElement(item);
        item.click();
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
        btnCancel.click();
        return new CatalogueManagementPage();
    }

    public CatalogueViewPage clickSaveButton() {
        btnSave.click();
        return new CatalogueViewPage();
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
