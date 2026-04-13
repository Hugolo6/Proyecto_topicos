package com.example.proyecto

object CarritoManager {
    private val items = mutableListOf<Producto>()

    fun agregarProducto(producto: Producto) {
        items.add(producto)
    }

    fun eliminarProducto(producto: Producto) {
        items.remove(producto)
    }

    fun obtenerProductos(): List<Producto> = items

    fun limpiarCarrito() {
        items.clear()
    }

    fun obtenerTotal(): Double {
        // Sumamos el precio con el descuento ya aplicado de cada producto
        return items.sumOf { it.precio * (1 - it.descuento) }
    }
}
