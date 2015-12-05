package com.epam.core.components.element;

import ch.lambdaj.Lambda;
import ch.lambdaj.function.convert.Converter;
import com.epam.core.common.CommonTimeouts;
import com.epam.core.components.AbstractPageElement;
import com.epam.core.driver.Driver;
import com.epam.core.driver.DriverUnit;
import com.epam.core.exceptions.HtmlElementsException;
import com.epam.core.logging.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ch.lambdaj.Lambda.convert;
import static ch.lambdaj.Lambda.convertMap;
import static com.epam.core.components.element.Table.ListConverter.toListsConvertingEachItem;
import static com.epam.core.components.element.Table.MapConverter.toMapsConvertingEachValue;
import static com.epam.core.components.element.Table.WebElementToTextConverter.toText;
import static com.epam.core.components.element.Table.WebElementToTextConverter.toTextValues;

public class Table extends AbstractPageElement {

    public static final int ONE_CONST = 1;

    public Table(WebElement wrappedElement, String name, String page) {
        super(wrappedElement, name, page);
    }

    public List<String> getRowWithCellText(String text) {
        List<List<String>> rows = getRowsAsString();

        for (List<String> row : rows) {
            if (row.contains(text)) {
                Logger.logInfo("Row with cell text " + text + " was found in table.");
                return row;
            }
        }

        Logger.logWarning("Row with cell text " + text + " was not found in table.");
        return null;
    }

    public boolean selectCellByText(String text, int columnId) {
        List<WebElement> cells = getColumns().get(columnId);

        for (WebElement cell : cells) {
            if (cell.getText().contains(text)) {
                selectCell(cell);
                Logger.logInfo("Cell with text " + text + " was selected.");
                return true;
            }
        }

        throw new RuntimeException("No items can be selected by name: " + text);
    }

    public int getRowCount() {
        return getRows().size() - ONE_CONST;
    }

    private void selectCell(WebElement cell) {
        Actions builder = new Actions(Driver.getDefault());
        builder.moveToElement(cell).click(cell).build().perform();
        DriverUnit.waitForSpecifiedTimeout(CommonTimeouts.TIMEOUT_500_MS.getMilliSeconds());
    }



    /**
     * Returns table heading elements (contained in "th" tags).
     *
     * @return List with table heading elements.
     */
    public List<WebElement> getHeadings() {
        return getWrappedElement().findElements(By.xpath(".//th"));
    }

    /**
     * Returns text values of table heading elements (contained in "th" tags).
     *
     * @return List with text values of table heading elements.
     */
    public List<String> getHeadingsAsString() {
        return convert(getHeadings(), toTextValues());
    }

    /**
     * Returns table cell elements grouped by rows.
     *
     * @return List where each item is a table row.
     */
    public List<List<WebElement>> getRows() {
        List<List<WebElement>> rows = new ArrayList<List<WebElement>>();
        List<WebElement> rowElements = getWrappedElement().findElements(By.xpath(".//tr"));

        for (WebElement rowElement : rowElements) {
            rows.add(rowElement.findElements(By.xpath(".//td")));
        }
        return rows;
    }

    /**
     * Returns text values of table cell elements grouped by rows.
     *
     * @return List where each item is text values of a table row.
     */
    public List<List<String>> getRowsAsString() {
        return convert(getRows(), toListsConvertingEachItem(toTextValues()));
    }

    /**
     * Returns table cell elements grouped by columns.
     *
     * @return List where each item is a table column.
     */
    public List<List<WebElement>> getColumns() {
        List<List<WebElement>> columns = new ArrayList<List<WebElement>>();
        List<List<WebElement>> rows = getRows();

        if (rows.isEmpty()) {
            return columns;
        }

        int columnsNumber = rows.get(0).size();
        for (int i = 0; i < columnsNumber; i++) {
            List<WebElement> column = new ArrayList<WebElement>();
            for (List<WebElement> row : rows) {
                column.add(row.get(i));
            }
            columns.add(column);
        }

        return columns;
    }

    /**
     * Returns text values of table cell elements grouped by columns.
     *
     * @return List where each item is text values of a table column.
     */
    public List<List<String>> getColumnsAsString() {
        return convert(getColumns(), toListsConvertingEachItem(toTextValues()));
    }

    /**
     * Returns table cell element at i-th row and j-th column.
     *
     * @param row Row number
     * @param column Column number
     * @return Cell element at row and column index.
     */
    public WebElement getCellAt(int row, int column) {
        return getRows().get(row + ONE_CONST).get(column - ONE_CONST);
    }

    /**
     * Returns list of maps where keys are table headings and values are table row elements.
     */
    public List<Map<String, WebElement>> getRowsMappedToHeadings() {
        return getRowsMappedToHeadings(getHeadingsAsString());
    }

    /**
     * Returns list of maps where keys are passed headings and values are table row elements.
     *
     * @param headings List containing strings to be used as table headings.
     */
    public List<Map<String, WebElement>> getRowsMappedToHeadings(List<String> headings) {
        List<Map<String, WebElement>> rowsMappedToHeadings = new ArrayList<Map<String, WebElement>>();
        List<List<WebElement>> rows = getRows();

        if (rows.isEmpty()) {
            return rowsMappedToHeadings;
        }

        for (List<WebElement> row : rows) {
            if (row.size() != headings.size()) {
                throw new HtmlElementsException("Headings count is not equal to number of cells in row");
            }

            Map<String, WebElement> rowToHeadingsMap = new HashMap<String, WebElement>();
            int cellNumber = 0;
            for (String heading : headings) {
                rowToHeadingsMap.put(heading, row.get(cellNumber));
                cellNumber++;
            }
            rowsMappedToHeadings.add(rowToHeadingsMap);
        }

        return rowsMappedToHeadings;
    }

    /**
     * Same as {@link #getRowsMappedToHeadings()} but retrieves text from row elements.
     */
    public List<Map<String, String>> getRowsAsStringMappedToHeadings() {
        return getRowsAsStringMappedToHeadings(getHeadingsAsString());
    }

    /**
     * Same as {@link #getRowsMappedToHeadings(java.util.List)} but retrieves text from row elements.
     */
    public List<Map<String, String>> getRowsAsStringMappedToHeadings(List<String> headings) {
        return convert(getRowsMappedToHeadings(headings), toMapsConvertingEachValue(toText()));
    }


    /* Inner utility converters */

    /**
     * Converts {@link WebElement} to text contained in it
     */
    static class WebElementToTextConverter implements Converter<WebElement, String> {

        public static Converter<WebElement, String> toText() {
            return new WebElementToTextConverter();
        }

        public static Converter<WebElement, String> toTextValues() {
            return new WebElementToTextConverter();
        }

        private WebElementToTextConverter() {
        }

        @Override
        public String convert(WebElement element) {
            return element.getText();
        }
    }

    /**
     * Converts {@code List&lt;F&gt;} to {@code List&lt;T&gt;} by applying specified converter to each list element.
     */
    static class ListConverter<F, T> implements Converter<List<F>, List<T>> {
        private final Converter<F, T> itemsConverter;

        public static <F, T> Converter<List<F>, List<T>> toListsConvertingEachItem(Converter<F, T> itemsConverter) {
            return new ListConverter<F, T>(itemsConverter);
        }

        private ListConverter(Converter<F, T> itemsConverter) {
            this.itemsConverter = itemsConverter;
        }

        @Override
        public List<T> convert(List<F> list) {
            return Lambda.convert(list, itemsConverter);
        }
    }

    /**
     * Converts {@code Map&lt;K, F&gt;} to {@code Map&lt;K, T&gt;} by applying specified converter to each value
     * in a map.
     */
    static class MapConverter<K, F, T> implements Converter<Map<K, F>, Map<K, T>> {
        private final Converter<F, T> valueConverter;

        public static <F, T> Converter<Map<String, F>, Map<String, T>> toMapsConvertingEachValue(Converter<F, T> valueConverter) {
            return new MapConverter<String, F, T>(valueConverter);
        }

        private MapConverter(Converter<F, T> valueConverter) {
            this.valueConverter = valueConverter;
        }

        @Override
        public Map<K, T> convert(Map<K, F> map) {
            return convertMap(map, valueConverter);
        }
    }





    // OLD IMPLEMENTATION
    /*public static final String ROW_XPATH_LOCATOR = "//tbody/tr";

    private static String getCellXpathLocator(int row, int column) {
        return ROW_XPATH_LOCATOR + "[" + row + "]/td[" + column + "]";
    }

    public int getRowCount() {
        return findAllByXPath(ROW_XPATH_LOCATOR).size();
    }

    public String getCellValue(int row, int column) {
        Cell cell = new Cell(row, column, this.name, this.page);
        return cell.getText();
    }

    public class Cell extends AbstractPageElement {
        private int row;
        private int column;

        public Cell(WebElement wrappedElement, String name, String page) {
            super(wrappedElement, name, page);
        }

        public Cell(int row, int column, String name, String page) {
            super(Table.this.findByXPath(getCellXpathLocator(row, column)), name, page);
            this.row = row;
            this.column = column;
        }

        public String getText() {
            return wrappedElement.getText();
        }

        public Cell nextInRow() {
            return new Cell(row, column + 1, name, page);
        }

        public Cell previousInRow() {
            return new Cell(row, column - 1, name, page);
        }

        public Cell nextInColumn() {
            return new Cell(row + 1, column, name, page);
        }

        public Cell previousInColumn() {
            return new Cell(row - 1, column, name, page);
        }

    }*/
}
