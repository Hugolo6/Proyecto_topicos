package com.example.proyecto

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.databinding.ItemDireccionBinding

class DireccionAdapter(
    private val direcciones: List<Direccion>,
    private val onDeleteClick: (Direccion) -> Unit
) : RecyclerView.Adapter<DireccionAdapter.DireccionViewHolder>() {

    class DireccionViewHolder(val binding: ItemDireccionBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DireccionViewHolder {
        val binding = ItemDireccionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DireccionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DireccionViewHolder, position: Int) {
        val direccion = direcciones[position]
        holder.binding.tvCalleNumero.text = "${direccion.calle} ${direccion.numero}"
        holder.binding.tvColoniaCp.text = "${direccion.colonia}, C.P. ${direccion.codigo_postal}"
        holder.binding.tvCiudadEstado.text = "${direccion.ciudad}, ${direccion.estado}"

        holder.binding.btnEliminarDireccion.setOnClickListener {
            onDeleteClick(direccion)
        }
    }

    override fun getItemCount(): Int = direcciones.size
}
