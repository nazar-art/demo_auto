package com.epam.pages.adidas.catalogue;

import com.epam.core.components.WebFieldDecorator;
import com.epam.core.components.element.Button;
import com.epam.core.components.element.SelectorBox;
import com.epam.core.components.element.TextInput;
import com.epam.pages.PageObject;
import com.epam.pages.adidas.WelcomePage;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.HashMap;

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
    private Button introDate;

    @FindBy(id = "expDate")
    private Button exitDate;

    @FindBy(id = "cmsConfigurationID")
    private SelectorBox selectConfigurationSet;

    @FindBy(id = "description")
    private TextInput inputDescription;


}
