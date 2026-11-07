package com.nttdata.stepsdefinitions;

import com.nttdata.steps.AppConfigSteps;
import com.nttdata.steps.SauceLabsCartSteps;
import com.nttdata.support.ScreenshotAttacher;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriverException;

import static org.junit.Assert.assertTrue;

public class SauceLabsCartStepDefs {

    private static final String APP_ID = "com.saucelabs.mydemoapp.android";

    private final AppConfigSteps appConfigSteps = new AppConfigSteps();
    private final SauceLabsCartSteps cartSteps = new SauceLabsCartSteps();
    private Scenario scenario;

    @Before
    public void configurarScenario(Scenario scenario) {
        this.scenario = scenario;
    }

    @Given("estoy en la aplicación de SauceLabs")
    public void estoy_en_la_aplicacion_de_saucelabs() {
        appConfigSteps.iniciarAplicacion("saucelabs.properties");
        cartSteps.restablecerEstadoAplicacion();
    }

    @And("valido que carguen correctamente los productos en la galeria")
    public void valido_que_carguen_los_productos_en_la_galeria() {
        assertTrue(
                "La galeria no muestra los productos esperados",
                cartSteps.galeriaCargada()
        );
    }

    @When("agrego {int} del siguiente producto {string}")
    public void agrego_unidades_del_siguiente_producto(int unidades, String producto) {
        cartSteps.agregarProducto(producto, unidades);
    }

    @Then("valido el carrito de compra actualice correctamente")
    public void valido_que_el_carrito_de_compra_se_actualice_correctamente() {
        try {
            assertTrue(
                    "El carrito no refleja el producto y la cantidad agregados",
                    cartSteps.carritoActualizado()
            );
        } finally {
            ScreenshotAttacher.attach(scenario, "Validacion del carrito");
        }
    }

    @After(order = 0)
    public void cerrarAplicacionAlFinalizar() {
        cerrarAplicacion();
    }

    private void cerrarAplicacion() {
        try {
            appConfigSteps.cerrarAplicacion(APP_ID);
        } catch (WebDriverException e) {
            System.err.println("[SauceLabsCartStepDefs] No se pudo cerrar la sesion Appium: " + e.getMessage());
        }
    }
}
