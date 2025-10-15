package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import support.esperas;


import java.util.List;

public class ProductosPage {
    private final WebDriver driver;
    private final esperas waits;

    private final By title = By.cssSelector(".title");
    private final By firstItemAddBtn = By.cssSelector(".inventory_list .inventory_item button.btn");
    private final By cartLink = By.cssSelector(".shopping_cart_link");
    private final By cartBadge = By.cssSelector(".shopping_cart_badge");
    private final By sortDropdown = By.cssSelector(".product_sort_container");
    private final By optionHiLo = By.cssSelector(".product_sort_container option[value='hilo']");
    private final By itemPrices = By.cssSelector(".inventory_item_price");

    public ProductosPage(WebDriver driver) {
        this.driver = driver;
        this.waits = new esperas(driver);
    }

    public boolean isLoaded() {
        return waits.visible(title).getText().trim().equalsIgnoreCase("Products");
    }

    public void addFirstItemToCart() {
        waits.click(firstItemAddBtn);
        // después del click aparece badge "1"
        waits.visible(cartBadge);
    }

    public void goToCart() {
        waits.click(cartLink);
        waits.titleTextIs("Your Cart");
        waits.urlContains("cart");
    }

    public void sortByPriceHighToLow() {
        waits.click(sortDropdown);
        waits.click(optionHiLo);
        // deja respirar un instante a la lista (ya hay espera implícita por DOM)
        waits.visible(itemPrices);
    }

    public double getFirstPrice() {
        List<WebElement> prices = driver.findElements(itemPrices);
        return parsePrice(prices.get(0).getText());
    }

    public double getLastPrice() {
        List<WebElement> prices = driver.findElements(itemPrices);
        return parsePrice(prices.get(prices.size() - 1).getText());
    }

    private double parsePrice(String text) {
        return Double.parseDouble(text.replace("$", "").trim());
    }
}
