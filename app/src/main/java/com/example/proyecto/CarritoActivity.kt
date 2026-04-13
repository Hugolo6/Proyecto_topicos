package com.example.proyecto

import android.os.Bundle
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

        // Configurar RecyclerView con callback para eliminar
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
        val subtotal = CarritoManager.obtenerSubtotal()
        val total = CarritoManager.obtenerTotal()
        val impuestos = total - subtotal

        binding.tvSubtotal.text = "$${String.format("%.2f", subtotal)}"
        binding.tvImpuestos.text = "$${String.format("%.2f", impuestos)}"
        binding.tvTotal.text = "$${String.format("%.2f", total)}"
    }
}
