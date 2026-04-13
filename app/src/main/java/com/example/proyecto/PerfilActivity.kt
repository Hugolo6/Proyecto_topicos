package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.proyecto.databinding.ActivityPerfilBinding
import com.google.firebase.auth.FirebaseAuth

class PerfilActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPerfilBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Perfil"

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        // Mostrar datos del usuario actual si existe
        auth.currentUser?.let { user ->
            binding.tvCorreoUsuario.text = user.email
            binding.tvNombreUsuario.text = user.displayName ?: "Usuario TechStore"
        }

        binding.btnMisPedidos.setOnClickListener {
            startActivity(Intent(this, MisPedidosActivity::class.java))
        }

        binding.btnDirecciones.setOnClickListener {
            startActivity(Intent(this, DireccionesActivity::class.java))
        }

        binding.btnIrLogin.setOnClickListener {
            val intent = Intent(this, InicioSesionActivity::class.java)
            // Clear stack to prevent back button from returning to profile if they log out
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        binding.btnCerrarSesion.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, InicioSesionActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}
