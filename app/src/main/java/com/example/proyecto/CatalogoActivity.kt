package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
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
        actualizarDatosMenu()
    }

    private fun actualizarDatosMenu() {
        val user = auth.currentUser
        if (user != null) {
            val headerView = binding.menuLateral.getHeaderView(0)
            val tvNombre = headerView.findViewById<TextView>(R.id.tv_nombre_nav)
            val tvCorreo = headerView.findViewById<TextView>(R.id.tv_correo_nav)

            // Intentar obtener el nombre desde Firestore si no está en el perfil de Auth
            db.collection("Usuarios").document(user.uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val nombreCompleto = document.getString("nombre_completo")
                        tvNombre.text = nombreCompleto ?: user.email
                    } else {
                        tvNombre.text = user.displayName ?: user.email
                    }
                }
                .addOnFailureListener {
                    tvNombre.text = user.displayName ?: user.email
                }
            
            tvCorreo.text = user.email
        }
    }

    override fun onResume() {
        super.onResume()
        actualizarDatosMenu()
    }

    private fun cargarProductos() {
        todasLosProductos.clear()
        
        // Producto 1
        todasLosProductos.add(Producto(
            "1", "HP OMEN 16 Slim", 
            "Laptop Gamer de alto rendimiento con procesador Intel Core Ultra 9 y gráficos NVIDIA RTX 5070.", 
            "• Procesador: Intel Core Ultra 9 185H\n• GPU: NVIDIA GeForce RTX 5070 8GB GDDR6\n• Memoria: 32GB DDR5 5600MHz\n• Almacenamiento: 1TB SSD NVMe Gen4\n• Pantalla: 16\" QHD+ 240Hz OLED", 
            43990.0, 0.15, 10, "Laptops", "laptop1"
        ))

        // Producto 2
        todasLosProductos.add(Producto(
            "2", "UGREEN USB C Hub", 
            "Hub 5 en 1 para expandir la conectividad de tu laptop con puertos 4K HDMI y USB 3.0.", 
            "• 1x HDMI 4K@60Hz\n• 3x USB 3.0 (5Gbps)\n• 1x USB-C PD 100W\n• Carcasa de aluminio premium\n• Plug and Play", 
            150.20, 0.10, 50, "Accesorios", "adaptador2"
        ))

        // Producto 3
        todasLosProductos.add(Producto(
            "3", "Soporte KIROGILY", 
            "Soporte ergonómico de acero al carbono, plegable y ajustable para laptops y tablets.", 
            "• Material: Acero al Carbono reforzado\n• Ajuste: 6 niveles de altura\n• Compatibilidad: 10 a 17 pulgadas\n• Diseño: Plegable y portátil\n• Ventilación: Mejora el flujo de aire", 
            450.0, 0.0, 30, "Accesorios", "soporte3"
        ))

        // Producto 4
        todasLosProductos.add(Producto(
            "4", "HUAWEI Pura 80", 
            "Smartphone insignia con la mejor cámara del mercado y diseño vanguardista.", 
            "• Cámara: Sistema XMAGE Ultra Chroma\n• Memoria: 12GB RAM + 256GB ROM\n• Pantalla: LTPO OLED 120Hz\n• Batería: 5000mAh con carga ultra rápida\n• Conectividad: 5G y Satelital", 
            10969.0, 0.13, 20, "Smartphones", "telefono4"
        ))

        filtrarPorCategoria("Todos")
    }

    private fun filtrarPorCategoria(categoria: String) {
        productosFiltrados.clear()
        if (categoria == "Todos") {
            productosFiltrados.addAll(todasLosProductos)
        } else if (categoria == "Ofertas") {
            productosFiltrados.addAll(todasLosProductos.filter { it.descuento > 0 })
        } else {
            productosFiltrados.addAll(todasLosProductos.filter { it.id_categoria.equals(categoria, ignoreCase = true) })
        }
        adapter.notifyDataSetChanged()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_inicio -> filtrarPorCategoria("Todos")
            R.id.nav_categorias -> {
                // Podrías abrir un diálogo de categorías o simplemente scroll a los Chips
            }
            R.id.nav_ofertas -> filtrarPorCategoria("Ofertas")
            R.id.nav_perfil -> startActivity(Intent(this, PerfilActivity::class.java))
            R.id.nav_carrito -> startActivity(Intent(this, CarritoActivity::class.java))
            R.id.nav_cerrar_sesion -> {
                auth.signOut()
                Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                actualizarDatosMenu() // Limpiará los campos si el usuario es nulo
                // Opcional: reiniciar actividad para ver cambios
                recreate()
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
