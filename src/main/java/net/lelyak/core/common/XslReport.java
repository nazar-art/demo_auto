package net.lelyak.core.common;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

public final class XslReport {

    private XslReport() {
    }

    public static void generateHtmlReport() {
        TransformerFactory tFactory = TransformerFactory.newInstance();

        try {
            Source xslDoc = new StreamSource("report.xsl");
            Source xmlDoc = new StreamSource("./target/failsafe-reports/testng-results.xml");

            File outputFile = new File("./target/failsafe-reports/custom_report.html");
            if (!outputFile.exists()) {
                outputFile.createNewFile();
            }
            OutputStream htmlFile = new FileOutputStream(outputFile);
            Transformer transformer = tFactory.newTransformer(xslDoc);
            transformer.transform(xmlDoc, new StreamResult(htmlFile));
        } catch (IOException | TransformerException e) {
            e.getMessage();
        }

    }
}
