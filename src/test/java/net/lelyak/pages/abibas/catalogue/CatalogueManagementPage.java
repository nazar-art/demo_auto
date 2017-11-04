package net.lelyak.pages.abibas.catalogue;

import net.lelyak.core.annotations.Page;
import net.lelyak.core.common.CommonTimeouts;
import net.lelyak.core.components.WebFieldDecorator;
import net.lelyak.core.components.element.Button;
import net.lelyak.core.components.element.CheckBox;
import net.lelyak.core.components.element.Table;
import net.lelyak.core.components.element.TextInput;
import net.lelyak.core.driver.Driver;
import net.lelyak.core.driver.DriverUnit;
import net.lelyak.core.logging.Logger;
import net.lelyak.core.utils.ElementUtils;
import net.lelyak.pages.PageObject;
import net.lelyak.pages.abibas.WelcomePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.HashMap;
import java.util.List;

@Page(title = "CatalogueManagementPage")
public class CatalogueManagementPage extends WelcomePage {

    public CatalogueManagementPage() {
        successor = new HashMap<Class<? extends PageObject>, Button>();
        PageFactory.initElements(new WebFieldDecorator(getDriver()), this);
    }

    @Override
    public boolean exist() {
        return btnAddNewCatalogue.isPresent()
                && tableCatalogues.isPresent()
                && checkBoxShowExpired.isPresent();
    }

    @FindBy(xpath = "id('container')//a[contains(text(), 'Add New Catalogue')]")
    protected Button btnAddNewCatalogue;

    @FindBy(id = "catalogue")
    protected Table tableCatalogues;

    @FindBy(xpath = "id('container')//span[contains(text(), 'Show expired')]")
    protected CheckBox checkBoxShowExpired;

    @FindBy(id = "filter")
    protected TextInput inputSearch;

    @FindBy(css = ".btn-search")
    protected Button btnSearch;



    public AddNewCataloguePage clickNewCatalogueBtn() {
        btnAddNewCatalogue.click();
        return new AddNewCataloguePage();
    }


    public boolean verifyThatCatalogueIsNotCreated(String catalogueShortName) {
        boolean result = false;
        searchFilter(catalogueShortName);

        WebElement notSearchResult = Driver
                    .findByXpath("//span[contains(text(), 'Nothing to display for')]");

            result = ElementUtils.waitForReady(notSearchResult);
            if (notSearchResult.isDisplayed()) {
                result = true;
            }
        return result;
    }

    public void searchFilter(String catalogueShortName) {
        inputSearch.sendText(catalogueShortName);
        btnSearch.click();
    }

    public boolean verifyThatCatalogueIsCreated(String catalogueShortName) {
        searchFilter(catalogueShortName);
        boolean result = false;

        List<String> rows = tableCatalogues.getRowWithCellText(catalogueShortName);
        if (rows.size() > 0) {
            result = true;
        }

        return result;
    }

    public CatalogueViewPage viewCatalogueDetails(String catalogueName) {
        /*List<WebElement> catalogueRow = tableCatalogues.getRows().get(0);
        WebElement catalogue = catalogueRow.get(0);
        catalogue.click();*/
        DriverUnit.waitForSpecifiedTimeout(CommonTimeouts.TIMEOUT_500_MS.getMilliSeconds());
        new CatalogueManagementPage(); // refresh page here

        boolean res = tableCatalogues.clickCellByTextInRow(catalogueName);
        if (res) {
            Logger.logInfo("View page will be open");
        } else {
            Logger.logWarning("Can not find catalogue in table - " + catalogueName);
        }
        return new CatalogueViewPage();
    }
}
