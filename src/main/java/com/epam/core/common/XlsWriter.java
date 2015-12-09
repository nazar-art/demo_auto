package com.epam.core.common;

import com.epam.core.logging.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.List;
import java.util.Map;

public final class XlsWriter {

    public void writeDataToExcelFile(String fileName, String sheetName, List<Map<String, String>> dataList) {
        InputStream myxls;
        XSSFWorkbook wbr;

        try {
            File file = new File(fileName);
            myxls = new FileInputStream(file);

            wbr = new XSSFWorkbook(myxls);

            XSSFSheet sheetr = wbr.getSheet(sheetName);
            myxls.close();

            for (int i = 0; i < dataList.size(); i++) {
                XSSFRow newRow = sheetr.createRow(sheetr.getLastRowNum() + 1
                        + i);
                for (int j = 0; j < sheetr.getRow(0).getLastCellNum(); j++) {
                    XSSFCell newCell = newRow.createCell(j);
                    newCell.setCellType(Cell.CELL_TYPE_STRING);
                    newCell.setCellValue(dataList.get(i).get(
                            sheetr.getRow(0).getCell(j).toString()));
                }
                i++;
            }

            FileOutputStream outPutStream = null;
            try {

                file.delete();
                outPutStream = new FileOutputStream(fileName, true);
                wbr.write(outPutStream);
            } catch (IOException e) {
                Logger.logError(e.getMessage());
            } finally {
                if (outPutStream != null) {
                    try {
                        outPutStream.flush();
                        outPutStream.close();

                    } catch (IOException e) {
                        Logger.logError(e.getMessage());
                    }
                }
            }
        } catch (IOException e1) {
            Logger.logError(e1.getMessage());
        }
    }

}
