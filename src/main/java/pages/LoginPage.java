package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import support.Waits;

public class LoginPage {

    private WebDriver driver;
    private Waits waits;

    // Elementos
    private By usernameInput = By.id("user-name");
    private By passwordInput = By.id("password");
    private By loginBtn = By.id("login-button");
    private By errorMsg = By.cssSelector("h3[data-test='error']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.waits = new Waits(driver);
    }

    public LoginPage open() {
        driver.get("https://www.saucedemo.com/");
        return this;
    }

    public LoginPage typeUsername(String username) {
        waits.visible(usernameInput).clear();
        waits.visible(usernameInput).sendKeys(username);
        return this;
    }

    public LoginPage typePassword(String password) {
        waits.visible(passwordInput).clear();
        waits.visible(passwordInput).sendKeys(password);
        return this;
    }

    public void clickLogin() {
        // Solo hace click (sin esperar siempre Products)
        waits.click(loginBtn);
    }

    // Para login exitoso
    public void waitForProducts() {
        waits.titleTextIs("Products");
    }

    // Para login inválido
    public void waitForError() {
        waits.visible(errorMsg);
    }

    public String getErrorText() {
        return waits.visible(errorMsg).getText().trim();
    }
}
