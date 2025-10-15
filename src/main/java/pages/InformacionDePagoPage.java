package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import support.esperas;

public class InformacionDePagoPage {
    private final WebDriver driver;
    private final esperas waits;

    private final By firstName = By.id("first-name");
    private final By lastName = By.id("last-name");
    private final By zip = By.id("postal-code");
    private final By continueBtn = By.id("continue");

    public InformacionDePagoPage(WebDriver driver) {
        this.driver = driver;
        this.waits = new esperas(driver);
    }

    public void fill(String f, String l, String z) {
        waits.type(firstName, f);
        waits.type(lastName, l);
        waits.type(zip, z);
    }

    public void clickContinue() {
        waits.click(continueBtn);
        waits.titleTextIs("Checkout: Overview");
    }
}
