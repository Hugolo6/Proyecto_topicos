package com.example.proyecto

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyecto.databinding.ActivityProductoDetalleBinding

class ProductoDetalleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductoDetalleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductoDetalleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.barraHerramientas)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        binding.barraHerramientas.setNavigationOnClickListener {
            onBackPressed()
        }

        // Obtener datos del Intent
        val nombre = intent.getStringExtra("NOMBRE") ?: ""
        val resumen = intent.getStringExtra("DESCRIPCION") ?: ""
        val especificaciones = intent.getStringExtra("ESPECIFICACIONES") ?: ""
        val precio = intent.getDoubleExtra("PRECIO", 0.0)
        val imagenUrl = intent.getStringExtra("IMAGEN") ?: ""
        val categoria = intent.getStringExtra("CATEGORIA") ?: ""

        // Mostrar datos en la UI
        binding.tvNombreProducto.text = nombre
        binding.tvPrecioProducto.text = "$${String.format("%.2f", precio)}"
        binding.tvDescripcionProducto.text = resumen
        binding.tvEspecificacionesProducto.text = especificaciones

        // Cargar imagen local
        val imageResId = resources.getIdentifier(imagenUrl, "mipmap", packageName)
        if (imageResId != 0) {
            binding.ivImagenProducto.setImageResource(imageResId)
        }

        binding.btnAnadirAlCarrito.setOnClickListener {
            // Creamos un objeto producto temporal para añadirlo
            val producto = Producto(
                id_producto = intent.getStringExtra("PRODUCTO_ID") ?: "",
                nombre = nombre,
                descripcion = resumen,
                especificaciones = especificaciones,
                precio = precio,
                id_categoria = categoria,
                imagen_url = imagenUrl,
                descuento = 0.0 // Podrías pasar el descuento también si lo necesitas
            )
            CarritoManager.agregarProducto(producto)
            Toast.makeText(this, "$nombre añadido al carrito", Toast.LENGTH_SHORT).show()
        }
    }
}
