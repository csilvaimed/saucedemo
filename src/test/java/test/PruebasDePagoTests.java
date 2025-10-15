package test;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.*;
import report.Pasos;

public class PruebasDePagoTests extends BaseTest {

    @Test(
            testName = "Script #4: Flujo completo de compra E2E",
            description = "Login, agregar producto, checkout y verificar mensaje final"
    )
    public void endToEnd_checkout_shouldShowThankYou() {
        new LoginPage(driver).open()
                .typeUsername("standard_user")
                .typePassword("secret_sauce")
                .clickLogin();
        new LoginPage(driver).waitForProducts();

        ProductosPage products = new ProductosPage(driver);
        products.addFirstItemToCart();
        products.goToCart();
        Pasos.pass(driver, "Carrito con producto", true);

        CarritoPage cart = new CarritoPage(driver);
        cart.clickCheckout();
        Pasos.pass(driver, "Formulario de información de compra", true);

        InformacionDePagoPage info = new InformacionDePagoPage(driver);
        info.fill("Cristóbal", "Silva", "8320000");
        info.clickContinue();
        Pasos.pass(driver, "Revisión de compra", true);

        new ResumenDePagoPage(driver).clickFinish();
        Pasos.pass(driver, "Finalizar compra", true);

        String confirmation = new PagoCompletoPage(driver).getCompleteText();
        Pasos.pass(driver, "Mensaje final: " + confirmation, true);
        Assert.assertEquals(confirmation, "Thank you for your order!");
    }
}
