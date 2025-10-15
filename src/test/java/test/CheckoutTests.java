package test;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.*;
import report.Step;

public class CheckoutTests extends BaseTest {

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

        ProductsPage products = new ProductsPage(driver);
        products.addFirstItemToCart();
        products.goToCart();
        Step.pass(driver, "Carrito con producto", true);

        CartPage cart = new CartPage(driver);
        cart.clickCheckout();
        Step.pass(driver, "Formulario de información de compra", true);

        CheckoutInformationPage info = new CheckoutInformationPage(driver);
        info.fill("Cristóbal", "Silva", "8320000");
        info.clickContinue();
        Step.pass(driver, "Revisión de compra", true);

        new CheckoutOverviewPage(driver).clickFinish();
        Step.pass(driver, "Finalizar compra", true);

        String confirmation = new CheckoutCompletePage(driver).getCompleteText();
        Step.pass(driver, "Mensaje final: " + confirmation, true);
        Assert.assertEquals(confirmation, "Thank you for your order!");
    }
}
