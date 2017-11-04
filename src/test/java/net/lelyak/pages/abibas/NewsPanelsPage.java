package net.lelyak.pages.abibas;

import net.lelyak.core.annotations.Page;
import net.lelyak.core.components.WebFieldDecorator;
import net.lelyak.core.components.element.Button;
import net.lelyak.core.components.element.Table;
import net.lelyak.core.logging.Logger;
import net.lelyak.pages.PageObject;
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
