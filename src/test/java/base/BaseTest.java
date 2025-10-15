package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import report.Step;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class BaseTest {

    protected WebDriver driver;
    private Path tempProfileDir; // para Chrome/Edge

    public WebDriver getDriver() { return driver; }

    @BeforeMethod(alwaysRun = true)
    public void setUp() throws Exception {
        String browser = System.getProperty("browser", "edge").toLowerCase();

        switch (browser) {
            case "edge":
                driver = buildEdge(false);
                break;
            case "edge-headless":
                driver = buildEdge(true);
                break;
            case "chrome":
                driver = buildChrome(false);
                break;
            case "chrome-headless":
                driver = buildChrome(true);
                break;
            case "firefox":
                driver = buildFirefox();
                break;
            default:
                driver = buildEdge(false);
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();
        Step.info(driver, "Inicio de prueba - navegador listo (" + browser + ")", true);
    }

    private WebDriver buildEdge(boolean forceHeadless) throws Exception {
        WebDriverManager.edgedriver().setup();
        EdgeOptions opts = new EdgeOptions();

        // PERFIL LIMPIO
        tempProfileDir = Files.createTempDirectory("edge-profile-");
        Files.createDirectories(tempProfileDir.resolve("Default"));
        opts.addArguments("--user-data-dir=" + tempProfileDir.toAbsolutePath());
        opts.addArguments("--profile-directory=Default");
        opts.addArguments("--password-store=basic"); // evita store del SO

        // PREFS (misma API que Chrome por ser Chromium)
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("autofill.profile_enabled", false);
        prefs.put("autofill.credit_card_enabled", false);
        prefs.put("autofill.address_enabled", false);
        opts.setExperimentalOption("prefs", prefs);

        // FLAGS para evitar avisos UI (password leak, onboarding, etc.)
        opts.addArguments("--disable-features=PasswordLeakDetection,PasswordCheck,PasswordManagerOnboarding,AutofillServerCommunication,NotificationTriggers");
        opts.addArguments("--disable-save-password-bubble");
        opts.addArguments("--disable-password-generation");
        opts.addArguments("--disable-infobars");
        opts.addArguments("--disable-notifications");
        opts.addArguments("--disable-popup-blocking");
        opts.addArguments("--disable-translate");
        opts.addArguments("--no-first-run");
        opts.addArguments("--no-default-browser-check");
        opts.addArguments("--start-maximized");

        if (forceHeadless || Boolean.parseBoolean(System.getProperty("headless", "false"))) {
            opts.addArguments("--headless=new");
            opts.addArguments("--window-size=1366,768");
        }

        return new EdgeDriver(opts);
    }

    private WebDriver buildChrome(boolean forceHeadless) throws Exception {
        WebDriverManager.chromedriver().setup();
        ChromeOptions opts = new ChromeOptions();

        // PERFIL LIMPIO
        tempProfileDir = Files.createTempDirectory("chrome-profile-");
        Files.createDirectories(tempProfileDir.resolve("Default"));
        opts.addArguments("--user-data-dir=" + tempProfileDir.toAbsolutePath());
        opts.addArguments("--profile-directory=Default");
        opts.addArguments("--password-store=basic");

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("autofill.profile_enabled", false);
        prefs.put("autofill.credit_card_enabled", false);
        prefs.put("autofill.address_enabled", false);
        opts.setExperimentalOption("prefs", prefs);

        opts.addArguments("--disable-features=PasswordLeakDetection,PasswordCheck,PasswordManagerOnboarding,AutofillServerCommunication,NotificationTriggers");
        opts.addArguments("--disable-save-password-bubble");
        opts.addArguments("--disable-password-generation");
        opts.addArguments("--disable-infobars");
        opts.addArguments("--disable-notifications");
        opts.addArguments("--disable-popup-blocking");
        opts.addArguments("--disable-translate");
        opts.addArguments("--no-first-run");
        opts.addArguments("--no-default-browser-check");
        opts.addArguments("--start-maximized");

        if (forceHeadless || Boolean.parseBoolean(System.getProperty("headless", "false"))) {
            opts.addArguments("--headless=new");
            opts.addArguments("--window-size=1366,768");
        }

        return new ChromeDriver(opts);
    }

    private WebDriver buildFirefox() {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions fo = new FirefoxOptions();
        if (Boolean.parseBoolean(System.getProperty("ff.headless", "false"))) {
            fo.addArguments("-headless");
        }
        fo.addPreference("signon.rememberSignons", false);
        fo.addPreference("signon.autofillForms", false);
        fo.addPreference("extensions.formautofill.creditCards.enabled", false);
        fo.addPreference("extensions.formautofill.addresses.enabled", false);
        return new FirefoxDriver(fo);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            try { Step.info(driver, "Cierre de navegador", true); } catch (Exception ignored) {}
            driver.quit();
        }
        // Limpia el perfil temporal
        if (tempProfileDir != null) {
            try {
                Files.walk(tempProfileDir)
                        .sorted((a,b) -> b.getNameCount() - a.getNameCount())
                        .forEach(p -> { try { Files.deleteIfExists(p); } catch (Exception ignored) {} });
            } catch (Exception ignored) {}
        }
    }

    protected void step(String message) { Step.info(driver, message, true); }
}
