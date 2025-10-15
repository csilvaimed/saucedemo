package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import support.Waits;

public class CheckoutCompletePage {
    private final WebDriver driver;
    private final Waits waits;

    private final By completeHeader = By.cssSelector(".complete-header"); // "Thank you for your order!"

    public CheckoutCompletePage(WebDriver driver) {
        this.driver = driver;
        this.waits = new Waits(driver);
    }

    public String getCompleteText() {
        return waits.visible(completeHeader).getText().trim();
    }
}
