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

    fun obtenerSubtotal(): Double {
        return items.sumOf { it.precio }
    }

    fun obtenerTotal(): Double {
        val subtotal = obtenerSubtotal()
        return subtotal + (subtotal * 0.16) // 16% IVA
    }
}
