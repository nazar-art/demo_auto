package net.lelyak.pages.abibas.productfeeds;

import net.lelyak.core.annotations.Page;
import net.lelyak.core.components.WebFieldDecorator;
import net.lelyak.core.components.element.Button;
import net.lelyak.pages.PageObject;
import net.lelyak.pages.abibas.WelcomePage;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.HashMap;

@Page(title = "ProductFeedsSeasonsPage")
public class SeasonsPage extends WelcomePage {

    public SeasonsPage() {
        successor = new HashMap<Class<? extends PageObject>, Button>();
        PageFactory.initElements(new WebFieldDecorator(getDriver()), this);
    }

    @Override
    public boolean exist() {
        return btnNewSeason.isPresent()
                && btnEditSeason.isPresent();
    }


    @FindBy(id = "newSeason")
    protected Button btnNewSeason;

    @FindBy(id = "editSeason")
    protected Button btnEditSeason;


}
