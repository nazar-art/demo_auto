package com.epam.core.common;

import com.epam.core.exceptions.XlsDataNotFoundException;
import com.epam.core.logging.Logger;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.*;

public final class XlsReader {

    private XSSFSheet sheet;
    private Map<String, String> data = new HashMap<String, String>();
    private List<String> metaData = new ArrayList<String>();
    private List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();

    public XlsReader() {
    }

    public XlsReader(String fileName, String sheetName) {
        open(fileName, sheetName);
    }

    public void open(String fileName, String sheetName) {
        InputStream fis = null;
        try {
            if (sheetName == null || sheetName.isEmpty()) {
                throw new IllegalArgumentException("Please, provide sheet name");
            }
            fis = new FileInputStream(fileName);
            XSSFWorkbook workBook = new XSSFWorkbook(fis);
            sheet = workBook.getSheet(sheetName);
            getMetaData();
            if (sheet == null) {
                throw new IllegalArgumentException(MessageFormat.format(
                        "Sheet is not found: {0}", sheetName));
            }
        } catch (IOException e) {
            Logger.logError(e.getMessage());
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    Logger.logError(e.getMessage());
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, String>> getDataListById(String testId) {
        Iterator<Row> rowIterator = sheet.rowIterator();
        if (!dataList.isEmpty()) {
            dataList.clear();
        }
        XSSFRow row;
        XSSFCell cell;
        Map<String, String> rowData;
        while ((rowIterator.hasNext())) {
            row = (XSSFRow) rowIterator.next();
            cell = row.getCell(0);
            if (cell != null
                    && cell.getStringCellValue().trim()
                    .equalsIgnoreCase(testId)) {
                rowData = new HashMap<String, String>();

                for (int i = 0; i < row.getLastCellNum() - 1; i++) {
                    cell = row.getCell(i);
                    try {
                        rowData.put(metaData.get(i), getCellValue(cell));
                    } catch (NullPointerException e) {
                        rowData.put(metaData.get(i), " ");
                    }
                }
                dataList.add(rowData);
            }
        }
        if (dataList.size() < 1) {
            throw new XlsDataNotFoundException("Data is not found by id: " + testId);
        }
        return (List<Map<String, String>>) SerializationUtils.clone((Serializable) dataList);
    }

    public Map<String, String> getDataById(String testId) {
        Iterator<Row> rowIterator = sheet.rowIterator();
        if (!data.isEmpty()) {
            data.clear();
        }
        XSSFRow row;
        XSSFCell cell;
        while ((rowIterator.hasNext())) {
            row = (XSSFRow) rowIterator.next();
            cell = row.getCell(0);
            if (cell != null
                    && cell.getStringCellValue().trim()
                    .equalsIgnoreCase(testId)) {
                for (int i = 0; i < row.getLastCellNum() - 1; i++) {
                    cell = row.getCell(i);
                    try {
                        data.put(metaData.get(i), getCellValue(cell));
                    } catch (NullPointerException e) {
                        data.put(metaData.get(i), " ");
                    }
                }
            }
        }
        if (data.isEmpty()) {
            throw new XlsDataNotFoundException("Data is not found by id: " + testId);
        }
        return data;
    }

    private String getCellValue(XSSFCell cell) {
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
            case Cell.CELL_TYPE_NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
        }
        return null;
    }

    private void getMetaData() {
        if (!metaData.isEmpty()) {
            metaData.clear();
        }
        XSSFRow row = sheet.getRow(1);
        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()) {
            metaData.add(cellIterator.next().toString());
        }
        sheet.removeRow(row);
    }

}
