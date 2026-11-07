package com.nttdata.steps;

import com.nttdata.screens.ProductosScreen;

public class SauceLabsCartSteps {

    private final ProductosScreen screen = new ProductosScreen();
    private String productoAgregado;
    private int unidadesAgregadas;

    public boolean galeriaCargada() {
        return screen.galeriaCargada();
    }

    public void restablecerEstadoAplicacion() {
        screen.restablecerEstadoAplicacion();
    }

    public void agregarProducto(String producto, int unidades) {
        screen.agregarProductoAlCarrito(producto, unidades);
        productoAgregado = producto;
        unidadesAgregadas = unidades;
    }

    public boolean carritoActualizado() {
        if (productoAgregado == null) {
            throw new IllegalStateException("Primero se debe agregar un producto al carrito");
        }
        return screen.carritoContiene(productoAgregado, unidadesAgregadas);
    }
}
