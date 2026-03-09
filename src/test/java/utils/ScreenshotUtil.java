package utils;

import base.BaseTest;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtil {
    private static final Logger logger = LoggerFactory.getLogger(ScreenshotUtil.class);

    public static void takeScreenshot(String stepName) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "screenshots/" + stepName.replaceAll("\\s+", "_") + "_" + timestamp + ".png";

        File srcFile = ((TakesScreenshot) BaseTest.getDriver()).getScreenshotAs(OutputType.FILE);
        File destFile = new File(fileName);

        try {
            Path destPath = destFile.toPath();
            Files.createDirectories(destPath.getParent());
            Files.copy(srcFile.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);
            logger.info("Ekran goruntusu alindi: " + destFile.getAbsolutePath());
        } catch (IOException e) {
            logger.error("Ekran goruntusu alinirken hata olustu: ", e);
        }
    }
}