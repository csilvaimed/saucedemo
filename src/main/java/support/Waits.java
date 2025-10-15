package support;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Waits {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public Waits(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(12));
    }

    public WebElement visible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement clickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public void click(By locator) {
        clickable(locator).click();
    }

    public void type(By locator, String text) {
        WebElement el = visible(locator);
        el.clear();
        el.sendKeys(text);
    }

    public void textPresent(By locator, String expected) {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, expected));
    }

    public void titleTextIs(String text) {
        textPresent(By.cssSelector(".title"), text);
    }

    public void urlContains(String fragment) {
        wait.until(ExpectedConditions.urlContains(fragment));
    }
}
