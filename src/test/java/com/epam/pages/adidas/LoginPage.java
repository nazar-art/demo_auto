package com.epam.pages.adidas;

import com.epam.core.annotations.Page;
import com.epam.core.common.CommonTimeouts;
import com.epam.core.components.WebFieldDecorator;
import com.epam.core.components.element.Button;
import com.epam.core.components.element.TextInput;
import com.epam.core.driver.DriverUnit;
import com.epam.pages.PageObject;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.HashMap;

@Page(title = "CommandCentreLoginPage")
public class LoginPage extends PageObject {

    public LoginPage() {
        successor = new HashMap<Class<? extends PageObject>, Button>();
        PageFactory.initElements(new WebFieldDecorator(getDriver()), this);
    }

    @Override
    public boolean exist() {
        return userLogin.isDisplayed()
                && userPass.isDisplayed()
                && submit.isDisplayed();
    }


    @FindBy(id = "username")
    protected TextInput userLogin;

    @FindBy(id = "password")
    protected TextInput userPass;

    @FindBy(name = "submit")
    protected Button submit;



    public WelcomePage login(String login, String pass) {
        userLogin.clear();
        userLogin.sendText(login);
        userPass.clear();
        userPass.sendText(pass);
        submit.click();
        return new WelcomePage();
    }

    @Deprecated
    public void navigateToModule() {
        getDriver().findElementByLinkText("News Panels").click();
        DriverUnit.waitForSpecifiedTimeout(CommonTimeouts.TIMEOUT_3_S.getMilliSeconds());
    }
}
