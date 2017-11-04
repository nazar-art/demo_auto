package net.lelyak.core.components.element;

import net.lelyak.core.components.AbstractPageElement;
import net.lelyak.core.localization.Localization;
import net.lelyak.core.logging.Logger;
import org.openqa.selenium.WebElement;

import java.util.List;

public class Selector extends AbstractPageElement {

    public Selector(WebElement wrappedElement, String name, String page) {
        super(wrappedElement, name, page);
    }

    /**
     * Constructs instance of {@link org.openqa.selenium.support.ui.Select} class.
     *
     * @return {@link org.openqa.selenium.support.ui.Select} class instance.
     */
    private org.openqa.selenium.support.ui.Select getSelect() {
        return new org.openqa.selenium.support.ui.Select(getWrappedElement());
    }

    /**
     * Indicates whether this select element support selecting multiple options at the same time.
     * This is done by checking the value of the "multiple" attribute.
     *
     * @return {@code true} if select element support selecting multiple options and {@code false} otherwise.
     */
    public boolean isMultiple() {
        return getSelect().isMultiple();
    }

    /**
     * Returns all options belonging to this select tag.
     *
     * @return A list of {@code WebElements} representing options.
     */
    public List<WebElement> getOptions() {
        return getSelect().getOptions();
    }

    /**
     * Returns all selected options belonging to this select tag.
     *
     * @return A list of {@code WebElements} representing selected options.
     */
    public List<WebElement> getAllSelectedOptions() {
        return getSelect().getAllSelectedOptions();
    }

    /**
     * The first selected option in this select tag (or the currently selected option in a normal select).
     *
     * @return A {@code WebElement} representing selected option.
     */
    public WebElement getFirstSelectedOption() {
        return getSelect().getFirstSelectedOption();
    }

    /**
     * Indicates if select has at least one selected option.
     *
     * @return {@code true} if select has at least one selected option and {@code false} otherwise.
     */
    public boolean hasSelectedOption() {
        for (WebElement option : getOptions()) {
            if (option.isSelected()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Select all options that display text matching the argument. That is, when given "Bar" this
     * would select an option like:
     * <p/>
     * &lt;option value="foo"&gt;Bar&lt;/option&gt;
     *
     * @param text The visible text to match against
     */
    public void selectByVisibleText(String text) {
        if (wrappedElement != null) {
            highlightElement();

            getSelect().selectByVisibleText(text);
            Logger.logInfo(Localization.getMessage(Localization.SELECT_VALUE, text, name, page));
        } else {
            Logger.logError(Localization.getMessage(Localization.NO_SELECT, name, page));
        }
    }

    /**
     * Select the option at the given index. This is done by examing the "index" attribute of an
     * element, and not merely by counting.
     *
     * @param index The option at this index will be selected
     */
    public void selectByIndex(int index) {
        if (wrappedElement != null) {
            highlightElement();
            getSelect().selectByIndex(index);
            Logger.logInfo(Localization
                    .getMessage(Localization.SELECT_VALUE, String.valueOf(index), name, page));
        }
    }

    /**
     * Select all options that have a value matching the argument. That is, when given "foo" this
     * would select an option like:
     * <p/>
     * &lt;option value="foo"&gt;Bar&lt;/option&gt;
     *
     * @param value The value to match against
     */
    public void selectByValue(String value) {
        if (wrappedElement != null) {
            highlightElement();
            getSelect().selectByValue(value);
            Logger.logInfo(Localization.getMessage(Localization.SELECT_VALUE, value, name, page));
        } else {
            Logger.logError(Localization.getMessage(Localization.NO_SELECT, name, page));
        }
    }

    /**
     * Clear all selected entries. This is only valid when the SELECT supports multiple selections.
     *
     * @throws UnsupportedOperationException If the SELECT does not support multiple selections
     */
    public void deselectAll() {
        getSelect().deselectAll();
    }

    /**
     * Deselect all options that have a value matching the argument. That is, when given "foo" this
     * would deselect an option like:
     * <p/>
     * &lt;option value="foo"&gt;Bar&lt;/option&gt;
     *
     * @param value The value to match against
     */
    public void deselectByValue(String value) {
        getSelect().deselectByValue(value);
    }

    /**
     * Deselect the option at the given index. This is done by examing the "index" attribute of an
     * element, and not merely by counting.
     *
     * @param index The option at this index will be deselected
     */
    public void deselectByIndex(int index) {
        getSelect().deselectByIndex(index);
    }

    /**
     * Deselect all options that display text matching the argument. That is, when given "Bar" this
     * would deselect an option like:
     * <p/>
     * &lt;option value="foo"&gt;Bar&lt;/option&gt;
     *
     * @param text The visible text to match against
     */
    public void deselectByVisibleText(String text) {
        getSelect().deselectByVisibleText(text);
    }

    /*public void selectRandomValue() {
        if (wrappedElement != null) {
            highlightElement();
            String value = "";
            Select select = new Select(wrappedElement);
            logAllOptions(select);
            Integer size = select.getOptions().size();
            Random random = new Random();
            Integer index = random.nextInt(size - 2) + 1;
            try {
                select.selectByIndex(index);
                value = select.getFirstSelectedOption().getText();
                Logger.logInfo(Localization.getMessage(Localization.SELECT_RANDOM, value, name, page));
            } catch (Exception e) {
                // log
            }
        } else {
            Logger.logError(Localization.getMessage(Localization.NO_SELECT, name, page));
        }
    }

    protected void logAllOptions(Select select) {
        List<WebElement> options = select.getOptions();
        StringBuilder builder = new StringBuilder("option = ");
        for (WebElement option : options) {
            builder.append(option.getText() + ", ");
        }
        Logger.logInfo(builder.toString());
    }

    public void selectValue(String valueToSelect) {
        boolean selected = false;
        if (wrappedElement != null) {
            highlightElement();
            Select select = new Select(wrappedElement);
            List<WebElement> options = select.getOptions();
            try {
                int index = -1;
                for (WebElement option : options) {
                    index = index + 1;
                    String optionText = option.getText();
                    if (optionText.contains(valueToSelect)
                            || optionText.equalsIgnoreCase(valueToSelect)) {
                        option.click();
                        Logger.logInfo(Localization.getMessage(Localization.SELECT_VALUE, valueToSelect, name,
                                page));
                        selected = true;
                        break;
                    }
                    *//*else {
                        select.selectByIndex(index);
						options.get(0).click();
					}*//*
                }
            } catch (NullPointerException e) {
                int index = options.size() - 1;
                options.get(index).click();
            }

            if (!selected) {
                Logger.logError(Localization.getMessage(Localization.SELECT_DATA_WRONG, valueToSelect, name,
                        page));
            }
        } else {
            Logger.logError(Localization.getMessage(Localization.NO_SELECT, name, page));
        }
    }

    public boolean isInOptions(String value) {
        if (wrappedElement != null) {
            highlightElement();
            Select select = new Select(wrappedElement);
            List<WebElement> options = select.getOptions();

            for (WebElement option : options) {
                if (option.getText().equals(value) && option.isDisplayed()) {
                    return true;
                }

            }
            return false;
        }
        Logger.logError(Localization.getMessage(Localization.NO_SELECT, name,
                page));
        return false;

    }

    public String getSelectedOptionText() {
        if (wrappedElement != null) {
            highlightElement();
            Select select = new Select(wrappedElement);
            String value = select.getFirstSelectedOption().getText();
            Logger.logInfo(Localization.getMessage( Localization.SELECT_GET_TEXT, value, name, page));
            return value;
        } else {
            Logger.logError(Localization.getMessage(Localization.NO_SELECT, name, page));
        }
        return null;
    }*/
}
