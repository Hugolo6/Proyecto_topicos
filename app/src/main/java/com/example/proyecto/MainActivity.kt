package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val auth = FirebaseAuth.getInstance()
        
        // Verificar si el usuario ya inició sesión
        if (auth.currentUser != null) {
            startActivity(Intent(this, CatalogoActivity::class.java))
        } else {
            startActivity(Intent(this, InicioSesionActivity::class.java))
        }
        finish()
    }
}