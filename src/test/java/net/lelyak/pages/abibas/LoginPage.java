package net.lelyak.pages.abibas;

import net.lelyak.core.annotations.Page;
import net.lelyak.core.components.WebFieldDecorator;
import net.lelyak.core.components.element.Button;
import net.lelyak.core.components.element.TextInput;
import net.lelyak.pages.PageObject;
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
}
