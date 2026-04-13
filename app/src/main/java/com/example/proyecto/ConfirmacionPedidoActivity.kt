package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.proyecto.databinding.ActivityConfirmacionPedidoBinding
import kotlin.random.Random

class ConfirmacionPedidoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfirmacionPedidoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmacionPedidoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Generar un número de orden aleatorio para la vista
        val numeroOrden = Random.nextInt(100000, 999999)
        binding.tvNumeroOrden.text = "Orden #${numeroOrden}"

        binding.btnSeguirComprando.setOnClickListener {
            // Regresar al catálogo limpiando el stack de actividades
            val intent = Intent(this, CatalogoActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Si el usuario presiona atrás, también lo mandamos al catálogo de forma limpia
        val intent = Intent(this, CatalogoActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }
}
