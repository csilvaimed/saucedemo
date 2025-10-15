package listeners;

import base.BaseTest;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.*;

import org.testng.annotations.Test; // <- importante para leer la anotación
import report.ReportManager;

public class ExtentTestNGListener implements ITestListener, ISuiteListener {

    private static final ThreadLocal<ExtentTest> CURRENT = new ThreadLocal<>();
    private ExtentReports extent;

    public static ExtentTest getTest() { return CURRENT.get(); }

    @Override
    public void onStart(ISuite suite) { extent = ReportManager.getInstance(); }

    @Override
    public void onFinish(ISuite suite) { if (extent != null) extent.flush(); }

    @Override
    public void onTestStart(ITestResult result) {
        // ===== Lee @Test(testName=..., description=...) =====
        java.lang.reflect.Method m = result.getMethod().getConstructorOrMethod().getMethod();
        Test testAnn = m.getAnnotation(Test.class);

        String displayName = (testAnn != null && testAnn.testName() != null && !testAnn.testName().isBlank())
                ? testAnn.testName()
                : result.getMethod().getMethodName();

        String desc = (testAnn != null && testAnn.description() != null) ? testAnn.description() : "";

        ExtentTest test = extent.createTest(displayName, desc);
        CURRENT.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest t = CURRENT.get();
        if (t != null) t.log(Status.PASS, "✅ Prueba OK");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest t = CURRENT.get();
        if (t == null) return;

        t.log(Status.FAIL, result.getThrowable());

        // Screenshot automático en fallo (si el test extiende BaseTest)
        WebDriver driver = extractDriver(result);
        if (driver != null) {
            String base64 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
            t.addScreenCaptureFromBase64String(base64, "Captura (fallo)");
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest t = CURRENT.get();
        if (t != null) {
            t.log(Status.SKIP, result.getThrowable() == null ? "⏭️ Prueba saltada" : result.getThrowable().toString());
        }
    }

    // ===== Util =====
    private WebDriver extractDriver(ITestResult result) {
        Object instance = result.getInstance();
        if (instance instanceof BaseTest) return ((BaseTest) instance).getDriver();
        return null;
    }
}
