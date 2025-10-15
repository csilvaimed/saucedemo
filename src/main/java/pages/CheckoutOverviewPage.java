package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import support.Waits;

public class CheckoutOverviewPage {
    private final WebDriver driver;
    private final Waits waits;

    private final By finishBtn = By.id("finish");

    public CheckoutOverviewPage(WebDriver driver) {
        this.driver = driver;
        this.waits = new Waits(driver);
    }

    public void clickFinish() {
        waits.click(finishBtn);
        // siguiente página es el complete
        waits.titleTextIs("Checkout: Complete!");
    }
}
