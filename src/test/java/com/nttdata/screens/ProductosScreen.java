package com.nttdata.screens;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static com.nttdata.screens.AppConfigScreen.getDriver;

public class ProductosScreen {

    private static final String APP_PACKAGE = "com.saucelabs.mydemoapp.android";
    private static final Duration TIMEOUT = Duration.ofSeconds(20);
    private static final List<String> PRODUCTOS_DEL_ESCENARIO = List.of(
            "Sauce Labs Backpack",
            "Sauce Labs Bolt - T-Shirt",
            "Sauce Labs Bike Light"
    );

    private final By tituloGaleria = id("productTV");
    private final By listaProductos = id("productRV");
    private final By ordenarProductos = id("sortIV");
    private final By precioDescendente = id("priceDesCL");
    private final By botonCarrito = id("cartRL");
    private final By menu = id("menuIV");
    private final By restablecerEstado = AppiumBy.xpath(
            "//android.widget.TextView[@resource-id='" + resourceId("itemTV") +
                    "' and @text='Reset App State']"
    );
    private final By confirmarRestablecimiento = AppiumBy.id("android:id/button1");
    private final By tituloCarrito = AppiumBy.xpath(
            "//android.widget.TextView[@resource-id='" + resourceId("productTV") + "' and @text='My Cart']"
    );
    private final By carritoVacio = id("noItemTitleTV");
    private final By botonIrAComprar = id("shoppingBt");
    private final By cantidadTotalCarrito = id("itemsTV");
    private final By botonAgregarAlCarrito =
            AppiumBy.xpath("//android.widget.Button[@content-desc=\"Tap to add product to cart\"]");
    private final By cantidadProducto = id("noTV");
    private final By aumentarCantidad = id("plusIV");

    public boolean galeriaCargada() {
        WebElement titulo = waitFor().until(ExpectedConditions.visibilityOfElementLocated(tituloGaleria));
        waitFor().until(ExpectedConditions.visibilityOfElementLocated(listaProductos));

        if (!"Products".equals(titulo.getText())) {
            return false;
        }

        for (String producto : PRODUCTOS_DEL_ESCENARIO) {
            buscarProductoEnGaleria(producto);
        }
        return true;
    }

    public void restablecerEstadoAplicacion() {
        waitFor().until(ExpectedConditions.visibilityOfElementLocated(tituloGaleria));
        waitFor().until(ExpectedConditions.elementToBeClickable(menu)).click();
        waitFor().until(ExpectedConditions.elementToBeClickable(restablecerEstado)).click();
        waitFor().until(ExpectedConditions.elementToBeClickable(confirmarRestablecimiento)).click();
        waitFor().until(ExpectedConditions.elementToBeClickable(confirmarRestablecimiento)).click();
        waitFor().until(ExpectedConditions.visibilityOfElementLocated(tituloGaleria));
        waitFor().until(ExpectedConditions.elementToBeClickable(botonCarrito)).click();
        waitFor().until(ExpectedConditions.visibilityOfElementLocated(carritoVacio));
        waitFor().until(ExpectedConditions.elementToBeClickable(botonIrAComprar)).click();
        waitFor().until(ExpectedConditions.visibilityOfElementLocated(tituloGaleria));
    }

    public void agregarProductoAlCarrito(String producto, int unidades) {
        if (unidades < 1) {
            throw new IllegalArgumentException("La cantidad de unidades debe ser mayor que cero");
        }

        seleccionarProducto(producto);
        waitFor().until(ExpectedConditions.visibilityOfElementLocated(botonAgregarAlCarrito));
        waitFor().until(ExpectedConditions.visibilityOfElementLocated(cantidadProducto));
        ajustarCantidad(unidades);
        waitFor().until(ExpectedConditions.elementToBeClickable(botonAgregarAlCarrito)).click();
    }

    public boolean carritoContiene(String producto, int unidades) {
        waitFor().until(ExpectedConditions.elementToBeClickable(botonCarrito)).click();
        waitFor().until(ExpectedConditions.visibilityOfElementLocated(tituloCarrito));

        By tituloProductoEnCarrito = AppiumBy.xpath(
                "//android.widget.TextView[@resource-id='" + resourceId("titleTV") +
                        "' and @text=" + xpathLiteral(nombreVisible(producto)) + "]"
        );
        waitFor().until(
                ExpectedConditions.visibilityOfElementLocated(tituloProductoEnCarrito)
        );
        By cantidadEnCarritoLocator = AppiumBy.xpath(
                "//android.widget.TextView[@resource-id='" + resourceId("titleTV") +
                        "' and @text=" + xpathLiteral(nombreVisible(producto)) +
                        "]/following::android.widget.TextView[@resource-id='" +
                        resourceId("noTV") + "'][1]"
        );
        String cantidadEnCarrito = waitFor().until(
                ExpectedConditions.visibilityOfElementLocated(cantidadEnCarritoLocator)
        ).getText();
        String cantidadTotal = waitFor().until(
                ExpectedConditions.visibilityOfElementLocated(cantidadTotalCarrito)
        ).getText();

        return String.valueOf(unidades).equals(cantidadEnCarrito)
                && (unidades + " Items").equals(cantidadTotal);
    }

    private void seleccionarProducto(String producto) {
        if ("Sauce Labs Bike Light".equals(producto)) {
            ordenarPorPrecioDescendente();
        }
        buscarProductoEnGaleria(producto);
        By imagenProducto = AppiumBy.xpath(
                "//android.widget.ImageView[@content-desc=" + xpathLiteral(nombreVisible(producto)) + "]"
        );
        waitFor().until(ExpectedConditions.elementToBeClickable(imagenProducto)).click();
    }

    private void ordenarPorPrecioDescendente() {
        waitFor().until(ExpectedConditions.elementToBeClickable(ordenarProductos)).click();
        waitFor().until(ExpectedConditions.elementToBeClickable(precioDescendente)).click();
        waitFor().until(ExpectedConditions.visibilityOfElementLocated(listaProductos));
    }

    private void ajustarCantidad(int unidades) {
        for (int cantidad = 1; cantidad < unidades; cantidad++) {
            waitFor().until(ExpectedConditions.elementToBeClickable(aumentarCantidad)).click();
            String cantidadEsperada = String.valueOf(cantidad + 1);
            waitFor().until(ExpectedConditions.textToBe(cantidadProducto, cantidadEsperada));
        }
    }

    private WebElement buscarProductoEnGaleria(String producto) {
        String nombreVisible = nombreVisible(producto);
        String selector = "new UiScrollable(new UiSelector().resourceId(\"" +
                resourceId("scrollView") + "\")).scrollIntoView(new UiSelector().resourceId(\"" +
                resourceId("titleTV") + "\").text(\"" + escaparUiAutomator(nombreVisible) + "\"))";
        return waitFor().until(ExpectedConditions.visibilityOfElementLocated(
                AppiumBy.androidUIAutomator(selector)
        ));
    }

    private String nombreVisible(String producto) {
        if ("Sauce Labs Bolt - T-Shirt".equals(producto)) {
            return "Sauce Labs Bolt T-Shirt";
        }
        return producto;
    }

    private WebDriverWait waitFor() {
        return new WebDriverWait(driver(), TIMEOUT);
    }

    private AndroidDriver driver() {
        return getDriver();
    }

    private By id(String id) {
        return AppiumBy.id(resourceId(id));
    }

    private String resourceId(String id) {
        return APP_PACKAGE + ":id/" + id;
    }

    private String escaparUiAutomator(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private String xpathLiteral(String value) {
        if (!value.contains("'")) {
            return "'" + value + "'";
        }
        if (!value.contains("\"")) {
            return "\"" + value + "\"";
        }
        return "concat('" + value.replace("'", "',\"'\",'") + "')";
    }
}
