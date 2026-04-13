package com.example.proyecto

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyecto.databinding.ActivityCarritoBinding

class CarritoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCarritoBinding
    private lateinit var adapter: CarritoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarritoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Carrito"

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val productos = CarritoManager.obtenerProductos().toMutableList()
        adapter = CarritoAdapter(productos) { producto ->
            CarritoManager.eliminarProducto(producto)
            productos.remove(producto)
            adapter.notifyDataSetChanged()
            actualizarTotales()
        }
        
        binding.rvCarrito.layoutManager = LinearLayoutManager(this)
        binding.rvCarrito.adapter = adapter

        actualizarTotales()

        binding.btnPagar.setOnClickListener {
            if (CarritoManager.obtenerProductos().isNotEmpty()) {
                startActivity(android.content.Intent(this, PagoActivity::class.java))
            } else {
                android.widget.Toast.makeText(this, "El carrito está vacío", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun actualizarTotales() {
        val totalConDescuento = CarritoManager.obtenerTotal()
        
        // En el carrito mostramos el Subtotal (precio original sin descuento)
        // Y el total ya con el descuento aplicado
        val subtotalOriginal = CarritoManager.obtenerProductos().sumOf { it.precio }
        val ahorro = subtotalOriginal - totalConDescuento

        binding.layoutSubtotal.visibility = View.VISIBLE
        binding.tvSubtotal.text = "$${String.format("%.2f", subtotalOriginal)}"
        
        binding.layoutImpuestos.visibility = View.VISIBLE
        binding.labelImpuestos.text = "Descuento aplicado:"
        binding.tvImpuestos.text = "-$${String.format("%.2f", ahorro)}"
        binding.tvImpuestos.setTextColor(android.graphics.Color.YELLOW)

        binding.divisorPrecios.visibility = View.VISIBLE
        binding.tvTotal.text = "$${String.format("%.2f", totalConDescuento)}"
    }
}
