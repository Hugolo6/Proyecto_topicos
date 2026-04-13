package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.proyecto.databinding.ActivityPagoBinding

class PagoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPagoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPagoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Pago"

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.btnConfirmar.setOnClickListener {
            // Ir a la pantalla de confirmación
            startActivity(Intent(this, ConfirmacionPedidoActivity::class.java))
        }
    }
}
