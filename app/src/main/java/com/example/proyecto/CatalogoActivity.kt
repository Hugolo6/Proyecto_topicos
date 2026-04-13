package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.proyecto.databinding.ActivityCatalogoBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CatalogoActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityCatalogoBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCatalogoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Configurar Toolbar
        setSupportActionBar(binding.barraHerramientas)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Configurar Navigation Drawer
        val toggle = ActionBarDrawerToggle(
            this, binding.contenedorPrincipal, binding.barraHerramientas,
            R.string.abrir_drawer, R.string.cerrar_drawer
        )
        binding.contenedorPrincipal.addDrawerListener(toggle)
        toggle.syncState()

        binding.menuLateral.setNavigationItemSelectedListener(this)

        // Botón de carrito en la barra
        binding.ivCarritoBarra.setOnClickListener {
            startActivity(Intent(this, CarritoActivity::class.java))
        }

        // Aquí se cargarían los productos de Firestore
        cargarProductos()
    }

    private fun cargarProductos() {
        // Implementación básica para mostrar que funciona la conexión
        db.collection("Productos").get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Toast.makeText(this, "No hay productos disponibles", Toast.LENGTH_SHORT).show()
                }
                // Aquí conectarías con tu Adapter de RecyclerView
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al cargar productos: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_inicio -> {
                // Ya estamos en inicio
            }
            R.id.nav_perfil -> {
                startActivity(Intent(this, PerfilActivity::class.java))
            }
            R.id.nav_carrito -> {
                startActivity(Intent(this, CarritoActivity::class.java))
            }
            R.id.nav_cerrar_sesion -> {
                auth.signOut()
                startActivity(Intent(this, InicioSesionActivity::class.java))
                finish()
            }
            // Agrega los demás casos según necesites
        }
        binding.contenedorPrincipal.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (binding.contenedorPrincipal.isDrawerOpen(GravityCompat.START)) {
            binding.contenedorPrincipal.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}