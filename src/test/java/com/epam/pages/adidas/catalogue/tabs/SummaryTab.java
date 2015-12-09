package com.epam.pages.adidas.catalogue.tabs;

import com.epam.core.annotations.Page;
import com.epam.core.components.WebFieldDecorator;
import com.epam.core.components.element.Button;
import com.epam.pages.PageObject;
import com.epam.pages.adidas.catalogue.CatalogueViewPage;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.HashMap;

@Page(title = "SummaryTab")
public class SummaryTab extends CatalogueViewPage {

    public SummaryTab() {
        successor = new HashMap<Class<? extends PageObject>, Button>();
        PageFactory.initElements(new WebFieldDecorator(getDriver()), this);
    }

    @Override
    public boolean exist() {
        return btnEdit.isPresent()
                && btnDelete.isPresent();
    }

    @FindBy(xpath = "//a[@title='Edit']")
    protected Button btnEdit;

    @FindBy(xpath = "//a[@title='Delete Catalogue']")
    protected Button btnDelete;


}
