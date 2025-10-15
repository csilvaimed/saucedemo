package test;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.ProductosPage;
import report.Pasos;

public class ClasificacionTests extends BaseTest {

    @Test(
            testName = "Script #3: Ordenar por precio (de mayor a menor)",
            description = "Ordena los productos de mayor a menor precio y valida el orden"
    )
    public void sortByPriceHighToLow_shouldHaveFirstGreaterThanLast() {
        new LoginPage(driver).open()
                .typeUsername("standard_user")
                .typePassword("secret_sauce")
                .clickLogin();
        new LoginPage(driver).waitForProducts();

        ProductosPage products = new ProductosPage(driver);
        Assert.assertTrue(products.isLoaded(), "No se cargó Products");

        products.sortByPriceHighToLow();
        double first = products.getFirstPrice();
        double last = products.getLastPrice();

        Pasos.pass(driver, String.format("Primer precio: %.2f | Último: %.2f", first, last), true);
        Assert.assertTrue(first > last, "El orden de precios no es correcto");
    }
}
