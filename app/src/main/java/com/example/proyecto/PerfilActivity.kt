package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyecto.databinding.ActivityPerfilBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PerfilActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPerfilBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Perfil"

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        // Mostrar datos del usuario actual
        val currentUser = auth.currentUser
        if (currentUser != null) {
            binding.tvCorreoUsuario.text = currentUser.email
            
            // Obtener el nombre desde Firestore ya que displayName puede ser nulo
            db.collection("Usuarios").document(currentUser.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val nombre = document.getString("nombre_completo")
                        binding.tvNombreUsuario.text = nombre ?: "Usuario TechNexus"
                    } else {
                        binding.tvNombreUsuario.text = currentUser.displayName ?: "Usuario TechNexus"
                    }
                }
                .addOnFailureListener {
                    binding.tvNombreUsuario.text = currentUser.displayName ?: "Usuario TechNexus"
                }
        }

        binding.btnMisPedidos.setOnClickListener {
            startActivity(Intent(this, MisPedidosActivity::class.java))
        }

        binding.btnDirecciones.setOnClickListener {
            startActivity(Intent(this, DireccionesActivity::class.java))
        }

        binding.btnIrLogin.setOnClickListener {
            val intent = Intent(this, InicioSesionActivity::class.java)
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
