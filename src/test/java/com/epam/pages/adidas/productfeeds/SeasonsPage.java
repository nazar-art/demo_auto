package com.epam.pages.adidas.productfeeds;

import com.epam.core.annotations.Page;
import com.epam.core.components.WebFieldDecorator;
import com.epam.core.components.element.Button;
import com.epam.pages.PageObject;
import com.epam.pages.adidas.WelcomePage;
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
