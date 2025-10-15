package report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.IOException;
import java.nio.file.*;

public class ReportManager {
    private static ExtentReports extent;

    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            try {
                Path out = Path.of("target", "extent");
                Path img = out.resolve("img");

                // crear directorios
                Files.createDirectories(img);
                // limpiar imágenes viejas
                try (DirectoryStream<Path> ds = Files.newDirectoryStream(img)) {
                    for (Path p : ds) Files.deleteIfExists(p);
                }

                String reportPath = out.resolve("extent.html").toString();
                ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
                spark.config().setDocumentTitle("Automation Report");
                spark.config().setReportName("SauceDemo - Tests");
                spark.config().setTheme(Theme.DARK);

                extent = new ExtentReports();
                extent.attachReporter(spark);
                extent.setSystemInfo("Project", "TareaDEMO");
                extent.setSystemInfo("Runner", "TestNG");
                extent.setSystemInfo("OS", System.getProperty("os.name"));
                extent.setSystemInfo("Java", System.getProperty("java.version"));

            } catch (IOException e) {
                throw new RuntimeException("No se pudo inicializar ExtentReports", e);
            }
        }
        return extent;
    }
}
