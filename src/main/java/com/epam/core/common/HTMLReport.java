package com.epam.core.common;

import com.epam.core.logging.Logger;
import org.testng.Reporter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

public final class HTMLReport {

    public static void createThumbnail(File file, String fileName) {
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("Source file is not found");
        }

        BufferedImage img = new BufferedImage(140, 140, BufferedImage.TYPE_INT_RGB);

        try {
            img.createGraphics()
                    .drawImage(ImageIO.read(file).getScaledInstance(150, 150, Image.SCALE_SMOOTH), 0, 0, null);

            ImageIO.write(img, "png", new File("LoggerScreenshots/" + fileName + "_thmb.png"));
        } catch (IOException e) {

            Logger.logEnvironment(e.getMessage());
        }
    }

    public static void addScreenshot(String path) {
        Reporter.log("<script type='text/javascript'>"
                + "this.imagePreview = function(){"
                + "xOffset = 400;"
                + "yOffset = 30;"
                + "$(\"a.preview\").hover(function(e){"
                + "$(\"body\").append(\"<img id='preview' height='500' width='500' src='\"+ this.href +\"' style='position: absolute;' />\");"
                + "$(\"#preview\")"
                + ".css(\"top\",(e.pageY + xOffset) + \"px\")"
                + ".css(\"left\",(e.pageX + yOffset) + \"px\")"
                + ".fadeIn(\"fast\");" + "    }," + "function(){"
                + "$(\"#preview\").remove();" + "    });"
                + "$(\"a.preview\").mousemove(function(e){" + "$(\"#preview\")"
                + ".css(\"top\",(e.pageY - xOffset) + \"px\")"
                + ".css(\"left\",(e.pageX + yOffset) + \"px\");" + "});" + "};"
                + "</script>");
        // Don't remove! It's for test on the local environment
        Reporter.log(MessageFormat.format("<a href=''../../LoggerScreenshots/{0}'' class=''preview''><img src=''../../LoggerScreenshots/{1}'' alt=''gallery thumbnail'' /></a>",
                path, path.split("\\.png")[0] + "_thmb.png"));
        Reporter.log(MessageFormat.format("<a href=''../../LoggerScreenshots/{0}''>Download Screenshot</a>", path));
        // END of test on the local environment

        Reporter.log(MessageFormat
                .format("<a href=''{0}'' class=''preview''><img src=''{1}'' alt=''gallery thumbnail'' /></a>",
                        path, path.split("\\.png")[0] + "_thmb.png"));
        Reporter.log(MessageFormat.format(
                "<a href=''{0}''>Download Screenshot</a>", path));
    }
}
