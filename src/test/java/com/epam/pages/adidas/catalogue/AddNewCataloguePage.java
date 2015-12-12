package com.epam.pages.adidas.catalogue;

import com.epam.core.annotations.Page;
import com.epam.core.components.WebFieldDecorator;
import com.epam.core.components.element.Button;
import com.epam.core.components.element.TextBox;
import com.epam.core.components.element.TextInput;
import com.epam.core.driver.Driver;
import com.epam.core.logging.Logger;
import com.epam.core.utils.CalendarUtils;
import com.epam.core.utils.ElementUtils;
import com.epam.model.dto.CatalogueManagementDTO;
import com.epam.pages.PageObject;
import com.epam.pages.adidas.WelcomePage;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.HashMap;

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

    /**
     * Error messages:
     */
    @FindBy(id = "qtip-0-content")
    protected TextBox errorTheSameCatalogueAlreadyExists;


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

    public AddNewCataloguePage clickSaveButton() {
        btnSave.click();
        return this;
    }

    public boolean isCatalogReallyNew() {
        Logger.logDebug("Check if catalogue is really new");
        boolean isCatalogNew = false;

        try {
            if (!errorTheSameCatalogueAlreadyExists.visibilityOfElementWait()) {
                Logger.logInfo("Catalogue is new and unique");
                isCatalogNew = true;
            } else {
                Logger.logError("Duplicate of catalogue has already exists!! Please, create unique catalogue");
            }
        } catch (NoSuchElementException | TimeoutException e) {
            Logger.logInfo("Catalogue is new and unique");
            isCatalogNew = true;
        }

        Logger.logDebug("DONE: Check if catalogue is really new");
        return isCatalogNew;
    }
}
