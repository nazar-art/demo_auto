package net.lelyak.pages.bank;

import net.lelyak.core.annotations.Page;
import net.lelyak.core.components.WebFieldDecorator;
import net.lelyak.core.components.element.Button;
import net.lelyak.core.components.element.TextInput;
import net.lelyak.pages.PageObject;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.HashMap;

@Page(title = "Parabank Login Page")
public class BankLoginPage extends PageObject {

    public BankLoginPage() {
        successor = new HashMap<>();
        PageFactory.initElements(new WebFieldDecorator(getDriver()), this);
    }

    @Override
    public boolean exist() {
        return loginBtn.isPresent() 
                && loginInput.isPresent() 
                && passwordInput.isPresent();
    }

    @FindBy(name = "username")
    private TextInput loginInput;
    
    @FindBy(name = "password")
    private TextInput passwordInput;
    
    @FindBy(xpath = "//input[@value='Log In']")
    private Button loginBtn;

    public boolean login(String login, String pass) {
        loginInput.clear();
        loginInput.sendText(login);
        passwordInput.clear();
        passwordInput.sendText(pass);
        loginBtn.submit();
        return new HomePage().exist();
    }
}
