package com.epam.pages.adidas;

import com.epam.core.annotations.Page;
import com.epam.core.components.WebFieldDecorator;
import com.epam.core.components.element.Button;
import com.epam.core.components.element.Table;
import com.epam.core.logging.Logger;
import com.epam.pages.PageObject;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.HashMap;

@Page(title = "NewsPanels")
public class NewsPanelsPage extends WelcomePage {

    public NewsPanelsPage() {
        successor = new HashMap<Class<? extends PageObject>, Button>();
        PageFactory.initElements(new WebFieldDecorator(getDriver()), this);
    }

    @Override
    public boolean exist() {
        return btnAdd.isPresent();
    }

    @FindBy(id = "addNewsPanel")
    protected Button btnAdd;

    @FindBy(id = "newspanelList")
    protected Table tableOfPanels;


    @Deprecated
    public void printTableRowsInfo() { // todo remove later
        Logger.logInfo("rows at table: " + tableOfPanels.getRowCount());
        Logger.logInfo(tableOfPanels.getCellAt(1, 4).getText());
    }
}
