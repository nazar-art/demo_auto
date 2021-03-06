package net.lelyak.pages.abibas.catalogue;

import net.lelyak.core.annotations.Page;
import net.lelyak.core.common.CommonTimeouts;
import net.lelyak.core.components.WebFieldDecorator;
import net.lelyak.core.components.element.Button;
import net.lelyak.core.components.element.NavigationLink;
import net.lelyak.core.driver.DriverUnit;
import net.lelyak.pages.PageObject;
import net.lelyak.pages.abibas.WelcomePage;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.HashMap;

@Page(title = "CatalogueViewPave")
public class CatalogueViewPage extends WelcomePage {

    public CatalogueViewPage() {
        successor = new HashMap<Class<? extends PageObject>, Button>();
        PageFactory.initElements(new WebFieldDecorator(getDriver()), this);
    }

    @Override
    public boolean exist() {
        return linkReturnToCatalogues.isPresent();
    }


    @FindBy(xpath = "id('container')//a[contains(text(), 'Catalogue List')]")
    protected NavigationLink linkReturnToCatalogues;

    public CatalogueManagementPage returnToCataloguesPage() {
        linkReturnToCatalogues.click();
        DriverUnit.waitForSpecifiedTimeout(CommonTimeouts.TIMEOUT_2_S.getMilliSeconds());
        return new CatalogueManagementPage();
    }

    @FindBy(xpath = "//a[@title='Edit']")
    protected Button btnEdit;

    @FindBy(xpath = "//a[@title='Delete Catalogue']")
    protected Button btnDelete;

    @FindBy(xpath = "id('deleteCatalogueConfirmation')//a[contains(text(), 'Yes')]")
    protected Button btnYes;

    @FindBy(xpath = "id('deleteCatalogueConfirmation')//a[contains(text(), 'No')]")
    protected Button btnNo;

    public CatalogueManagementPage clickConfirmDeletion() {
        if (btnYes.visibilityOfElementWait()) {
            btnYes.click();
        }
        return new CatalogueManagementPage();
    }

    public CatalogueViewPage clickCancelDeletion() {
        if (btnNo.visibilityOfElementWait()) {
            btnNo.click();
        }
        return this;
    }

    public CatalogueViewPage clickDeleteButton() {
        btnDelete.click();
        return this;
    }


}
