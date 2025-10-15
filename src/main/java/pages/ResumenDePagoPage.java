package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import support.esperas;

public class ResumenDePagoPage {
    private final WebDriver driver;
    private final esperas waits;

    private final By finishBtn = By.id("finish");

    public ResumenDePagoPage(WebDriver driver) {
        this.driver = driver;
        this.waits = new esperas(driver);
    }

    public void clickFinish() {
        waits.click(finishBtn);
        // siguiente página es el complete
        waits.titleTextIs("Checkout: Complete!");
    }
}
