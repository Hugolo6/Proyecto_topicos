package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.proyecto.databinding.ActivityPagoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class PagoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPagoBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPagoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Finalizar Compra"

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        // Configuración inicial
        binding.rbEfectivo.isChecked = true

        binding.btnSeleccionarDireccion.setOnClickListener {
            mostrarDialogoSeleccionDireccion()
        }

        binding.btnConfirmar.setOnClickListener {
            procesarPedido()
        }
    }

    private fun mostrarDialogoSeleccionDireccion() {
        val uid = auth.currentUser?.uid ?: return
        
        db.collection("Usuarios").document(uid).collection("Direcciones")
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Toast.makeText(this, "No tienes direcciones guardadas", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                val direcciones = documents.toObjects(Direccion::class.java)
                val items = direcciones.map { "${it.calle} ${it.numero}, ${it.colonia}" }.toTypedArray()

                AlertDialog.Builder(this)
                    .setTitle("Selecciona una dirección")
                    .setItems(items) { _, which ->
                        val seleccionada = direcciones[which]
                        binding.etDireccion.setText("${seleccionada.calle} ${seleccionada.numero}, ${seleccionada.colonia}")
                        binding.etCiudad.setText("${seleccionada.ciudad}, ${seleccionada.estado}")
                    }
                    .show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al cargar direcciones", Toast.LENGTH_SHORT).show()
            }
    }

    private fun procesarPedido() {
        val uid = auth.currentUser?.uid ?: return
        val direccion = binding.etDireccion.text.toString().trim()
        val telefono = binding.etTelefono.text.toString().trim()
        val ciudad = binding.etCiudad.text.toString().trim()
        
        val metodoPago = if (binding.rbEfectivo.isChecked) "Efectivo" else "Tarjeta"

        if (direccion.isEmpty() || telefono.isEmpty() || ciudad.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos de envío", Toast.LENGTH_SHORT).show()
            return
        }

        val productos = CarritoManager.obtenerProductos()
        if (productos.isEmpty()) {
            Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show()
            return
        }

        binding.btnConfirmar.isEnabled = false
        
        val total = CarritoManager.obtenerTotal()
        val orderId = UUID.randomUUID().toString().substring(0, 8).uppercase()

        val pedido = hashMapOf(
            "id_pedido" to orderId,
            "id_usuario" to uid,
            "fecha_pedido" to Calendar.getInstance().time,
            "total" to total,
            "direccion_envio" to direccion,
            "ciudad" to ciudad,
            "telefono" to telefono,
            "metodo_pago" to metodoPago,
            "estado" to "Pendiente"
        )

        db.collection("Pedidos").document(orderId)
            .set(pedido)
            .addOnSuccessListener {
                guardarDetallesPedido(orderId)
            }
            .addOnFailureListener {
                binding.btnConfirmar.isEnabled = true
                Toast.makeText(this, "Error al procesar el pedido", Toast.LENGTH_SHORT).show()
            }
    }

    private fun guardarDetallesPedido(orderId: String) {
        val batch = db.batch()
        val productos = CarritoManager.obtenerProductos()

        for (producto in productos) {
            val detalleRef = db.collection("DetallesPedido").document()
            val detalle = hashMapOf(
                "id_pedido" to orderId,
                "id_producto" to producto.id_producto,
                "nombre_producto" to producto.nombre,
                "cantidad" to 1,
                "precio_unitario" to producto.precio * (1 - producto.descuento)
            )
            batch.set(detalleRef, detalle)
        }

        batch.commit()
            .addOnSuccessListener {
                CarritoManager.limpiarCarrito()
                val intent = Intent(this, ConfirmacionPedidoActivity::class.java)
                intent.putExtra("ORDER_ID", orderId)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                CarritoManager.limpiarCarrito()
                startActivity(Intent(this, ConfirmacionPedidoActivity::class.java))
                finish()
            }
    }
}
