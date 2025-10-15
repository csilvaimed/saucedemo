package test;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CarritoPage;
import pages.LoginPage;
import pages.ProductosPage;
import report.Pasos;

public class LoginTests extends BaseTest {

    @Test(
            testName = "Script #1: Login + agregar primer ítem y verificar en carrito",
            description = "Login exitoso con usuario estándar, agrega el primer producto y valida en el carrito"
    )
    public void addItemToCart_afterLogin_success() {
        LoginPage login = new LoginPage(driver).open()
                .typeUsername("standard_user")
                .typePassword("secret_sauce");
        login.clickLogin();
        login.waitForProducts();

        ProductosPage products = new ProductosPage(driver);
        Assert.assertTrue(products.isLoaded(), "No se cargó la página Products");
        products.addFirstItemToCart();
        products.goToCart();

        CarritoPage cart = new CarritoPage(driver);
        Assert.assertTrue(
                cart.containsItem("Sauce Labs Backpack"),
                "El producto 'Sauce Labs Backpack' no está en el carrito"
        );
        Pasos.pass(driver, "Producto verificado en el carrito", true);
    }

    @Test(
            testName = "Script #2: Login inválido (usuario bloqueado)",
            description = "Valida que el usuario bloqueado muestre mensaje de error"
    )
    public void invalidLogin_lockedOutUser_showsError() {
        LoginPage login = new LoginPage(driver).open()
                .typeUsername("locked_out_user")
                .typePassword("secret_sauce");
        login.clickLogin();
        login.waitForError();

        String esperado = "Epic sadface: Sorry, this user has been locked out.";
        String actual = login.getErrorText();
        Pasos.pass(driver, "Mensaje de error mostrado: " + actual, true);
        Assert.assertEquals(actual, esperado);
    }
}
