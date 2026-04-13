package com.example.proyecto

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.databinding.ItemProductoBinding

class ProductoAdapter(private val productos: List<Producto>) :
    RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    class ProductoViewHolder(val binding: ItemProductoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val binding = ItemProductoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]
        holder.binding.tvNombre.text = producto.nombre
        holder.binding.tvPrecio.text = "$${producto.precio}"
        holder.binding.tvCategoria.text = "Categoría: ${producto.id_categoria}"
        
        // Cargar imagen local dinámica (p1, p2, p3...)
        val context = holder.itemView.context
        val imageResId = context.resources.getIdentifier(producto.imagen_url, "mipmap", context.packageName)
        if (imageResId != 0) {
            holder.binding.ivProducto.setImageResource(imageResId)
        }

        // Click para ver detalle
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductoDetalleActivity::class.java).apply {
                putExtra("PRODUCTO_ID", producto.id_producto)
                putExtra("NOMBRE", producto.nombre)
                putExtra("DESCRIPCION", producto.descripcion) // Resumen
                putExtra("ESPECIFICACIONES", producto.especificaciones) // Full
                putExtra("PRECIO", producto.precio)
                putExtra("CATEGORIA", producto.id_categoria)
                putExtra("IMAGEN", producto.imagen_url)
            }
            context.startActivity(intent)
        }

        holder.binding.btnAgregarCarrito.setOnClickListener {
            CarritoManager.agregarProducto(producto)
            Toast.makeText(context, "${producto.nombre} agregado al carrito", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = productos.size
}
