package com.epam.core.common;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

public class XslReport {

    private XslReport() {

    }

    public static void generateHtmlReport() {
        TransformerFactory tFactory = TransformerFactory.newInstance();

        try {
            Source xslDoc = new StreamSource("report.xsl");
            Source xmlDoc = new StreamSource(
                    "./target/failsafe-reports/testng-results.xml");

            File outputFile = new File(
                    "./target/failsafe-reports/custom_report.html");
            if (!outputFile.exists()) {
                outputFile.createNewFile();
            }
            OutputStream htmlFile = new FileOutputStream(outputFile);
            Transformer transformer = tFactory.newTransformer(xslDoc);
            transformer.transform(xmlDoc, new StreamResult(htmlFile));
        } catch (FileNotFoundException e) {
            e.getMessage();
        } catch (TransformerConfigurationException e) {
            e.getMessage();
        } catch (TransformerException e) {
            e.getMessage();
        } catch (IOException e) {
            e.getMessage(); // To change body of catch statement use File |
            // Settings | File Templates.
        }

    }
}
