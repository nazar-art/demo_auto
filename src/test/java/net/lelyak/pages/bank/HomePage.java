package net.lelyak.pages.bank;

import net.lelyak.core.annotations.Page;
import net.lelyak.core.components.WebFieldDecorator;
import net.lelyak.core.components.element.TextBox;
import net.lelyak.pages.PageObject;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.HashMap;

@Page(title = "Bank Home Page")
public class HomePage extends PageObject {

    public HomePage() {
        successor = new HashMap<>();
        PageFactory.initElements(new WebFieldDecorator(getDriver()), this);
    }

    @Override
    public boolean exist() {
        return title.isPresent();
    }

    @FindBy(css = ".title")
    private TextBox title;

    public String getTitleText() {
        return title.getText();
    }
}
