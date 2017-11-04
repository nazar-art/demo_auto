package net.lelyak.core.components.element;

import net.lelyak.core.components.AbstractPageElement;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.List;

public class Radio extends AbstractPageElement {

    public Radio(WebElement wrappedElement, String name, String page) {
        super(wrappedElement, name, page);
    }

    public List<WebElement> getButtons() {
        String radioName = getWrappedElement().getAttribute("name");

        String xpath;
        if (radioName == null) {
            xpath = "self::* | following::input[@type = 'radio'] | preceding::input[@type = 'radio']";
        } else {
            xpath = String.format(
                    "self::* | following::input[@type = 'radio' and @name = '%s'] | " +
                            "preceding::input[@type = 'radio' and @name = '%s']",
                    radioName, radioName);
        }

        return getWrappedElement().findElements(By.xpath(xpath));
    }

    /**
     * Returns selected radio button.
     *
     * @return {@code WebElement} representing selected radio button or {@code null} if no radio buttons are selected.
     */
    public WebElement getSelectedButton() {
        for (WebElement button : getButtons()) {
            if (button.isSelected()) {
                return button;
            }
        }

        throw new NoSuchElementException("No selected button");
    }

    /**
     * Indicates if radio has selected button.
     *
     * @return {@code true} if radio has selected button and {@code false} otherwise.
     */
    public boolean hasSelectedButton() {
        for (WebElement button : getButtons()) {
            if (button.isSelected()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Selects radio button that have a value matching the specified argument.
     *
     * @param value The value to match against.
     */
    public void selectByValue(String value) {
        for (WebElement button : getButtons()) {
            String buttonValue = button.getAttribute("value");
            if (value.equals(buttonValue)) {
                selectButton(button);
                return;
            }
        }

        throw new NoSuchElementException(String.format("Cannot locate radio button with value: %s", value));
    }

    /**
     * Selects radio button by the given index.
     *
     * @param index Index of a radio button to be selected.
     */
    public void selectByIndex(int index) {
        List<WebElement> buttons = getButtons();

        if (index < 0 || index >= buttons.size()) {
            throw new NoSuchElementException(String.format("Cannot locate radio button with index: %d", index));
        }

        selectButton(buttons.get(index));
    }

    /**
     * Selects radio button uf it's not already selected.
     *
     * @param button {@code WebElement} representing radio button to be selected.
     */
    private void selectButton(WebElement button) {
        if (!button.isSelected()) {
            button.click();
        }
    }
}
