# language: es
Característica: Carrito de compras en SauceLabs

  Como usuario de la aplicación móvil de SauceLabs
  Quiero agregar productos al carrito
  Para validar que se actualicen las cantidades correctamente

  @SauceLabsCart @Smoke
  Esquema del escenario: Agregar productos al carrito
    Dado estoy en la aplicación de SauceLabs
    Y valido que carguen correctamente los productos en la galeria
    Cuando agrego <UNIDADES> del siguiente producto "<PRODUCTO>"
    Entonces valido el carrito de compra actualice correctamente

    Ejemplos:
      | PRODUCTO                  | UNIDADES |
      | Sauce Labs Backpack       | 1        |
      | Sauce Labs Bolt - T-Shirt | 1        |
      | Sauce Labs Bike Light     | 2        |
