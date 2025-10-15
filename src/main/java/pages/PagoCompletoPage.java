package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import support.esperas;

public class PagoCompletoPage {
    private final WebDriver driver;
    private final esperas waits;

    private final By completeHeader = By.cssSelector(".complete-header"); // "Thank you for your order!"

    public PagoCompletoPage(WebDriver driver) {
        this.driver = driver;
        this.waits = new esperas(driver);
    }

    public String getCompleteText() {
        return waits.visible(completeHeader).getText().trim();
    }
}
