package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import support.Waits;

public class CartPage {
    private final WebDriver driver;
    private final Waits waits;

    private final By cartItemNames = By.cssSelector(".cart_item .inventory_item_name");
    private final By checkoutBtn = By.id("checkout");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.waits = new Waits(driver);
    }

    public boolean containsItem(String name) {
        return driver.findElements(cartItemNames).stream()
                .anyMatch(el -> name.equalsIgnoreCase(el.getText().trim()));
    }

    public void clickCheckout() {
        waits.click(checkoutBtn);
        waits.titleTextIs("Checkout: Your Information");
    }
}
