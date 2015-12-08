package com.epam.pages.adidas.catalogue;

import com.epam.core.annotations.Page;
import com.epam.core.components.WebFieldDecorator;
import com.epam.core.components.element.Button;
import com.epam.core.components.element.CheckBox;
import com.epam.core.components.element.Table;
import com.epam.pages.PageObject;
import com.epam.pages.adidas.WelcomePage;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.HashMap;

@Page(title = "CatalogueManagementPage")
public class CatalogueManagementPage extends WelcomePage {

    public CatalogueManagementPage() {
        successor = new HashMap<Class<? extends PageObject>, Button>();
        PageFactory.initElements(new WebFieldDecorator(getDriver()), this);
    }

    @Override
    public boolean exist() {
        return btnAddNewCatalogue.isPresent()
                && tblCatalogues.isPresent()
                && checkBoxShowExpired.isPresent();
    }

    @FindBy(xpath = "id('container')//a[contains(text(), 'Add New Catalogue')]")
    protected Button btnAddNewCatalogue;

    @FindBy(id = "catalogue")
    protected Table tblCatalogues;

    @FindBy(xpath = "id('container')//span[contains(text(), 'Show expired')]")
    protected CheckBox checkBoxShowExpired;


    public AddNewCataloguePage clickNewCatalogueBtn() {
        btnAddNewCatalogue.click();
        return new AddNewCataloguePage();
    }


    public boolean verifyThatCatalogueIsNotCreated(String catalogueName) {
        // todo looking at table
        return false;
    }
}
