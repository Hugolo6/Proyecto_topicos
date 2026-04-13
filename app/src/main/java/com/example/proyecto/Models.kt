package com.example.proyecto

import com.google.firebase.Timestamp

data class Usuario(
    val id_usuario: String = "",
    val nombre_completo: String = "",
    val correo: String = "",
    val contrasena: String = ""
)

data class Categoria(
    val id_categoria: String = "",
    val nombre_categoria: String = ""
)

data class Producto(
    val id_producto: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val especificaciones: String = "",
    val precio: Double = 0.0,
    val descuento: Double = 0.0, // Porcentaje de descuento (ej: 0.15 para 15%)
    val stock: Int = 0,
    val id_categoria: String = "",
    val imagen_url: String = ""
)

data class Pedido(
    val id_pedido: String = "",
    val id_usuario: String = "",
    val fecha_pedido: Timestamp = Timestamp.now(),
    val total: Double = 0.0,
    val direccion_envio: String = "",
    val ciudad: String = "",
    val telefono: String = "",
    val metodo_pago: String = ""
)

data class DetallesPedido(
    val id_detalle: String = "",
    val id_pedido: String = "",
    val id_producto: String = "",
    val cantidad: Int = 0,
    val precio_unitario: Double = 0.0
)