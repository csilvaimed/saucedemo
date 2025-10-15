package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import support.Waits;

public class CheckoutInformationPage {
    private final WebDriver driver;
    private final Waits waits;

    private final By firstName = By.id("first-name");
    private final By lastName = By.id("last-name");
    private final By zip = By.id("postal-code");
    private final By continueBtn = By.id("continue");

    public CheckoutInformationPage(WebDriver driver) {
        this.driver = driver;
        this.waits = new Waits(driver);
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
