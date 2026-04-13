package com.example.proyecto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyecto.databinding.ActivityDireccionesBinding
import com.example.proyecto.databinding.DialogAgregarDireccionBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DireccionesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDireccionesBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private val listaDirecciones = mutableListOf<Direccion>()
    private lateinit var adapter: DireccionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDireccionesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        setupRecyclerView()
        cargarDirecciones()

        binding.btnAgregarDireccion.setOnClickListener {
            mostrarDialogoAgregar()
        }
    }

    private fun setupRecyclerView() {
        adapter = DireccionAdapter(listaDirecciones) { direccion ->
            eliminarDireccion(direccion)
        }
        binding.rvDirecciones.layoutManager = LinearLayoutManager(this)
        binding.rvDirecciones.adapter = adapter
    }

    private fun cargarDirecciones() {
        val uid = auth.currentUser?.uid ?: return
        binding.pbCargando.visibility = View.VISIBLE

        db.collection("Usuarios").document(uid).collection("Direcciones")
            .addSnapshotListener { value, error ->
                binding.pbCargando.visibility = View.GONE
                if (error != null) {
                    Toast.makeText(this, "Error al cargar direcciones", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                listaDirecciones.clear()
                for (doc in value!!) {
                    val direccion = doc.toObject(Direccion::class.java).copy(id_direccion = doc.id)
                    listaDirecciones.add(direccion)
                }

                if (listaDirecciones.isEmpty()) {
                    binding.layoutVacio.visibility = View.VISIBLE
                    binding.rvDirecciones.visibility = View.GONE
                } else {
                    binding.layoutVacio.visibility = View.GONE
                    binding.rvDirecciones.visibility = View.VISIBLE
                }
                adapter.notifyDataSetChanged()
            }
    }

    private fun mostrarDialogoAgregar() {
        val builder = AlertDialog.Builder(this)
        val dialogBinding = DialogAgregarDireccionBinding.inflate(LayoutInflater.from(this))
        builder.setView(dialogBinding.root)
        val dialog = builder.create()

        dialogBinding.btnGuardarDireccion.setOnClickListener {
            val calle = dialogBinding.etCalle.text.toString().trim()
            val numero = dialogBinding.etNumero.text.toString().trim()
            val cp = dialogBinding.etCodigoPostal.text.toString().trim()
            val colonia = dialogBinding.etColonia.text.toString().trim()
            val ciudad = dialogBinding.etCiudad.text.toString().trim()
            val estado = dialogBinding.etEstado.text.toString().trim()
            val referencia = dialogBinding.etReferencia.text.toString().trim()

            if (calle.isEmpty() || numero.isEmpty() || cp.isEmpty() || colonia.isEmpty() || ciudad.isEmpty() || estado.isEmpty()) {
                Toast.makeText(this, "Por favor llena todos los campos obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            guardarDireccionEnFirestore(calle, numero, cp, colonia, ciudad, estado, referencia, dialog)
        }

        dialog.show()
    }

    private fun guardarDireccionEnFirestore(
        calle: String, num: String, cp: String, col: String,
        ciu: String, est: String, ref: String, dialog: AlertDialog
    ) {
        val uid = auth.currentUser?.uid ?: return
        val nuevaDireccion = hashMapOf(
            "id_usuario" to uid,
            "calle" to calle,
            "numero" to num,
            "codigo_postal" to cp,
            "colonia" to col,
            "ciudad" to ciu,
            "estado" to est,
            "referencia" to ref
        )

        db.collection("Usuarios").document(uid).collection("Direcciones")
            .add(nuevaDireccion)
            .addOnSuccessListener {
                Toast.makeText(this, "Dirección guardada con éxito", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al guardar la dirección", Toast.LENGTH_SHORT).show()
            }
    }

    private fun eliminarDireccion(direccion: Direccion) {
        val uid = auth.currentUser?.uid ?: return
        AlertDialog.Builder(this)
            .setTitle("Eliminar dirección")
            .setMessage("¿Estás seguro de que quieres eliminar esta dirección?")
            .setPositiveButton("Eliminar") { _, _ ->
                db.collection("Usuarios").document(uid).collection("Direcciones")
                    .document(direccion.id_direccion)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(this, "Dirección eliminada", Toast.LENGTH_SHORT).show()
                    }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
