package com.example.proyecto

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.databinding.ItemCarritoBinding

class CarritoAdapter(
    private val productos: MutableList<Producto>,
    private val onEliminarClick: (Producto) -> Unit
) : RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder>() {

    class CarritoViewHolder(val binding: ItemCarritoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoViewHolder {
        val binding = ItemCarritoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarritoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarritoViewHolder, position: Int) {
        val producto = productos[position]
        holder.binding.tvNombreCarrito.text = producto.nombre
        holder.binding.tvPrecioCarrito.text = "$${producto.precio}"
        holder.binding.tvCantidadCarrito.text = "Cantidad: 1"

        holder.binding.btnEliminar.setOnClickListener {
            onEliminarClick(producto)
        }
    }

    override fun getItemCount(): Int = productos.size
}
