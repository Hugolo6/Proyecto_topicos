package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyecto.databinding.ActivityRegistroBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source

class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        binding.botonRegistrarse.setOnClickListener {
            val nombre = binding.campoNombre.text.toString().trim()
            val correo = binding.campoCorreo.text.toString().trim()
            val contrasena = binding.campoContrasena.text.toString().trim()
            val confirmarContrasena = binding.campoConfirmarContrasena.text.toString().trim()

            if (nombre.isNotEmpty() && correo.isNotEmpty() && contrasena.isNotEmpty()) {
                if (contrasena == confirmarContrasena) {
                    if (binding.casillaTerminos.isChecked) {
                        
                        binding.progressBar.visibility = View.VISIBLE
                        binding.botonRegistrarse.isEnabled = false

                        auth.createUserWithEmailAndPassword(correo, contrasena)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val userId = auth.currentUser?.uid ?: ""
                                    
                                    val user = hashMapOf(
                                        "id_usuario" to userId,
                                        "nombre_completo" to nombre,
                                        "correo" to correo,
                                        "fecha_registro" to com.google.firebase.Timestamp.now()
                                    )
                                    
                                    // Guardar en Firestore
                                    db.collection("Usuarios").document(userId)
                                        .set(user)
                                        .addOnSuccessListener {
                                            binding.progressBar.visibility = View.GONE
                                            Toast.makeText(this, "¡Bienvenido, $nombre!", Toast.LENGTH_SHORT).show()
                                            startActivity(Intent(this, CatalogoActivity::class.java))
                                            finish()
                                        }
                                        .addOnFailureListener { e ->
                                            binding.progressBar.visibility = View.GONE
                                            binding.botonRegistrarse.isEnabled = true
                                            // Si llega aquí, el usuario se creó en Auth pero no en la BD
                                            Toast.makeText(this, "Usuario creado, pero error en BD: ${e.message}", Toast.LENGTH_LONG).show()
                                        }
                                } else {
                                    binding.progressBar.visibility = View.GONE
                                    binding.botonRegistrarse.isEnabled = true
                                    
                                    if (task.exception is FirebaseAuthUserCollisionException) {
                                        Toast.makeText(this, "Este correo ya está registrado. Intenta iniciar sesión.", Toast.LENGTH_LONG).show()
                                    } else {
                                        Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                    } else {
                        Toast.makeText(this, "Debes aceptar los términos", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Llena todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        binding.textoInicioSesion.setOnClickListener {
            finish()
        }
    }
}
