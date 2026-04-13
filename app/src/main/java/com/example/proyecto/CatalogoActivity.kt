package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyecto.databinding.ActivityCatalogoBinding
import com.google.android.material.chip.Chip
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CatalogoActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityCatalogoBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: ProductoAdapter
    private val todasLosProductos = mutableListOf<Producto>()
    private val productosFiltrados = mutableListOf<Producto>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCatalogoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        setSupportActionBar(binding.barraHerramientas)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val toggle = ActionBarDrawerToggle(
            this, binding.contenedorPrincipal, binding.barraHerramientas,
            R.string.abrir_drawer, R.string.cerrar_drawer
        )
        binding.contenedorPrincipal.addDrawerListener(toggle)
        toggle.syncState()

        binding.menuLateral.setNavigationItemSelectedListener(this)

        binding.ivCarritoBarra.setOnClickListener {
            startActivity(Intent(this, CarritoActivity::class.java))
        }

        adapter = ProductoAdapter(productosFiltrados)
        binding.rvProductos.layoutManager = LinearLayoutManager(this)
        binding.rvProductos.adapter = adapter

        binding.grupoCategorias.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                val chip = group.findViewById<Chip>(checkedIds.first())
                val categoria = chip.text.toString()
                filtrarPorCategoria(categoria)
            }
        }

        cargarProductos()
    }

    private fun cargarProductos() {
        todasLosProductos.clear()
        
        // El precio definido AQUÍ ya incluye IVA. El descuento se aplicará solo en el carrito.
        
        // Producto 1
        todasLosProductos.add(Producto(
            "1", "HP OMEN 16 Slim", 
            "Gaming Laptop, Intel Core Ultra 9, RTX 5070, 32GB RAM", 
            "Descripción completa...", 43990.0, 0.15, 10, "Laptops", "laptop1"
        ))

        // Producto 2
        todasLosProductos.add(Producto(
            "2", "UGREEN USB C Hub", 
            "5 en 1 Adaptador USB C Multipuerto", 
            "Descripción completa...", 150.20, 0.10, 50, "Accesorios", "adaptador2"
        ))

        // Producto 3
        todasLosProductos.add(Producto(
            "3", "Soporte KIROGILY", 
            "Acero Carbono, Plegable", 
            "Descripción completa...", 450.0, 0.0, 30, "Accesorios", "soporte3"
        ))

        // Producto 4
        todasLosProductos.add(Producto(
            "4", "HUAWEI Pura 80", 
            "12+256G, Cámara XMAGE Ultra Chroma", 
            "Descripción completa...", 10969.0, 0.13, 20, "Smartphones", "telefono4"
        ))

        filtrarPorCategoria("Todos")
    }

    private fun filtrarPorCategoria(categoria: String) {
        productosFiltrados.clear()
        if (categoria == "Todos") {
            productosFiltrados.addAll(todasLosProductos)
        } else {
            productosFiltrados.addAll(todasLosProductos.filter { it.id_categoria.equals(categoria, ignoreCase = true) })
        }
        adapter.notifyDataSetChanged()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_inicio -> {}
            R.id.nav_perfil -> startActivity(Intent(this, PerfilActivity::class.java))
            R.id.nav_carrito -> startActivity(Intent(this, CarritoActivity::class.java))
            R.id.nav_cerrar_sesion -> {
                auth.signOut()
                startActivity(Intent(this, InicioSesionActivity::class.java))
                finish()
            }
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
