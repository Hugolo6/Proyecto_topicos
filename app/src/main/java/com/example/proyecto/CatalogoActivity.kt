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

        // Configurar RecyclerView
        adapter = ProductoAdapter(productosFiltrados)
        binding.rvProductos.layoutManager = LinearLayoutManager(this)
        binding.rvProductos.adapter = adapter

        // Configurar Filtros de Categoría
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
        
        // Producto 1: Laptop (Descuento 15% aplicado)
        // Precio original 43990 -> 37391.50
        todasLosProductos.add(Producto(
            "1", 
            "HP OMEN 16 Slim", 
            "Gaming Laptop, Intel Core Ultra 9, RTX 5070, 32GB RAM", 
            "HP OMEN 16 Slim Gaming Laptop, visualización 2K de 16 pulgadas 144 Hz, Intel Core Ultra 9 285H, NVIDIA GeForce RTX 5070, AI VR Ready, teclado retroiluminado RGB, negro, Windows 11 Pro (32)\n\nMemoria: 32 GB DDR5 RAM.\nAlmacenamiento: SSD PCIe de 1 TB\nProcesador: Intel Core Ultra 9 285H\nGráficos: RTX 5070 8GB GDDR7\nPortátil AI con Intel AI Boost NPU\nPC Copilot+", 
            37391.50, 10, "Laptops", "laptop1"
        ))

        // Producto 2: Adaptador (Descuento 10% aplicado)
        // Precio original 150.20 -> 135.18
        todasLosProductos.add(Producto(
            "2", 
            "UGREEN USB C Hub", 
            "5 en 1 Adaptador USB C Multipuerto Aluminio a 4K HDMI", 
            "UGREEN USB C Hub, 5 en 1 Adaptador USB C Multipuerto Aluminio a 4K 30Hz HDMI, 1 USB 3.0 y 2 USB 2.0, PD Carga Compatible con MacBook M1 M2 Pro Air iPad iPhone 16 Galaxy S24 Chromebook DELL XPS y Más", 
            135.18, 50, "Accesorios", "adaptador2"
        ))

        // Producto 3: Soporte
        todasLosProductos.add(Producto(
            "3", 
            "Soporte KIROGILY", 
            "Acero Carbono, Plegable, Estable y Antideslizante", 
            "Soporte para Laptop Acero Carbono, Base para Laptop Plegable Portátil Estable y Antideslizante, Base Enfriadora para Laptop Stand con Base Giratoria 360° y Altura Ajustable. Soporta hasta 10 kg, diseño hueco para disipación de calor, ajustable en altura y ángulo.", 
            450.0, 30, "Accesorios", "soporte3"
        ))

        // Producto 4: Teléfono (Descuento 13% aplicado)
        // Precio original 10969 -> 9543.03
        todasLosProductos.add(Producto(
            "4", 
            "HUAWEI Pura 80", 
            "12+256G, Cámara XMAGE Ultra Chroma, 5170 mAh", 
            "HUAWEI Pura 80 Celular, 12+256G, Cámara XMAGE Ultra Chroma, Cancelación de Ruido con IA, Gran Batería de 5170 mAh y Dual Supercharge, Cristal Mate y Diseño de Bordes Planos, Blanco. Kunlun Glass de 2da generación.", 
            9543.03, 20, "Smartphones", "telefono4"
        ))

        // Mostrar todos al inicio
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
