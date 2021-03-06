package net.lelyak.pages.abibas.catalogue;

import net.lelyak.core.annotations.Page;
import net.lelyak.core.components.WebFieldDecorator;
import net.lelyak.core.components.element.Button;
import net.lelyak.core.components.element.TextBox;
import net.lelyak.core.components.element.TextInput;
import net.lelyak.core.driver.Driver;
import net.lelyak.core.logging.Logger;
import net.lelyak.core.utils.CalendarUtils;
import net.lelyak.core.utils.ElementUtils;
import net.lelyak.pages.PageObject;
import net.lelyak.pages.abibas.WelcomePage;
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


    public AddNewCataloguePage fillNewCatalogueForm(String catalogueShortName,
                                                    String catalogueLongName, String confSet, String confDesc,
                                                    String exitDate, String introDate) {
        setShortName(catalogueShortName);
        setLongName(catalogueLongName);

        CalendarUtils.setDate(inputIntroDate, introDate);
        CalendarUtils.setDate(inputExitDate, exitDate);

        setConfigurationSet(confSet);
        setDescription(confDesc);
        return this;
    }

    public AddNewCataloguePage setDescription(String catalogueDesc) {
        inputDescription.sendText(catalogueDesc);
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

    public AddNewCataloguePage setLongName(String catalogueLongName) {
        inputLongName.sendText(catalogueLongName);
        return this;
    }

    public AddNewCataloguePage setShortName(String catalogueShortName) {
        inputShortName.sendText(catalogueShortName);
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
