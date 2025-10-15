package report;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import listeners.ExtentTestNGListener;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Pasos {

    private static final DateTimeFormatter TS = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");
    private static final Path REPORT_DIR = Path.of("target", "extent");
    private static final Path IMG_DIR = REPORT_DIR.resolve("img");

    private static String savePngAndReturnRelative(WebDriver driver) throws Exception {
        Files.createDirectories(IMG_DIR);
        String fileName = "shot_" + TS.format(LocalDateTime.now()) + ".png";
        Path imgPath = IMG_DIR.resolve(fileName);

        byte[] png = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        Files.write(imgPath, png);

        // IMPORTANTE: ruta relativa desde extent.html -> "img/<file>"
        return ("img/" + fileName).replace("\\", "/");
    }

    public static void log(WebDriver driver, Status status, String message, boolean withScreenshot) {
        ExtentTest test = ExtentTestNGListener.getTest();
        if (test == null) return;

        try {
            if (withScreenshot && driver != null) {
                String rel = savePngAndReturnRelative(driver);
                test.log(status, message).addScreenCaptureFromPath(rel, message);
            } else {
                test.log(status, message);
            }
        } catch (Exception e) {
            test.log(status, message + " (sin screenshot por error: " + e.getMessage() + ")");
        }
    }

    public static void info(WebDriver d, String m, boolean ss) { log(d, Status.INFO, m, ss); }
    public static void pass(WebDriver d, String m, boolean ss) { log(d, Status.PASS, m, ss); }
    public static void fail(WebDriver d, String m, boolean ss) { log(d, Status.FAIL, m, ss); }
}
