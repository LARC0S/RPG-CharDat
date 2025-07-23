package ies.quevedo.chardat.framework.fragmentListPersonajes

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ies.quevedo.chardat.R
import ies.quevedo.chardat.databinding.CardPersonajeFragmentBinding
import ies.quevedo.chardat.domain.model.Personaje

class RVPersonajeAdapter(
    private val goMainMenu: (Int) -> Unit
) : ListAdapter<Personaje,
        RVPersonajeAdapter.ItemViewHolder>(PersonajeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.card_personaje_fragment, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        with(holder) {
            val item = getItem(position)
            bind(item, goMainMenu)
        }
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = CardPersonajeFragmentBinding.bind(itemView)

        @SuppressLint("SetTextI18n")
        fun bind(
            item: Personaje,
            goMainMenu: (Int) -> Unit
        ) = with(binding) {
            setImageClass(item)
            tvName.text = item.name
            tvClase.text = item.clase
            tvCreationDate.text = "Fecha de creación: ${item.creationDate}"
            cardPersonaje.setOnClickListener { goMainMenu(absoluteAdapterPosition) }
        }

        private fun setImageClass(item: Personaje) {
            when (item.clase) {
                "GUERRERO" -> binding.ivClaseIcono.setImageResource(R.drawable.guerrero_icono)
                "GUERRERO ACRÓBATA" -> binding.ivClaseIcono.setImageResource(R.drawable.guerrero_acrobata_icono)
                "PALADÍN" -> binding.ivClaseIcono.setImageResource(R.drawable.paladin_icono)
                "PALADÍN OSCURO" -> binding.ivClaseIcono.setImageResource(R.drawable.paladin_oscuro_icono)
                "MAESTRO DE ARMAS" -> binding.ivClaseIcono.setImageResource(R.drawable.maestro_de_armas_icono)
                "TECNICISTA" -> binding.ivClaseIcono.setImageResource(R.drawable.tecnicista_icono)
                "TAO" -> binding.ivClaseIcono.setImageResource(R.drawable.tao_icono)
                "EXPLORADOR" -> binding.ivClaseIcono.setImageResource(R.drawable.explorador_icono)
                "SOMBRA" -> binding.ivClaseIcono.setImageResource(R.drawable.sombra_icono)
                "LADRÓN" -> binding.ivClaseIcono.setImageResource(R.drawable.ladron_icono)
                "ASESINO" -> binding.ivClaseIcono.setImageResource(R.drawable.asesino_icono)
                "HECHICERO" -> binding.ivClaseIcono.setImageResource(R.drawable.hechicero_icono)
                "WARLOCK" -> binding.ivClaseIcono.setImageResource(R.drawable.warlock_icono)
                "ILUSIONISTA" -> binding.ivClaseIcono.setImageResource(R.drawable.ilusionista_icono)
                "HECHICERO MENTALISTA" -> binding.ivClaseIcono.setImageResource(R.drawable.hechicero_mentalista_icono)
                "CONJURADOR" -> binding.ivClaseIcono.setImageResource(R.drawable.conjurador_icono)
                "GUERRERO CONJURADOR" -> binding.ivClaseIcono.setImageResource(R.drawable.guerrero_conjurador_icono)
                "MENTALISTA" -> binding.ivClaseIcono.setImageResource(R.drawable.mentalista_icono)
                "GUERRERO MENTALISTA" -> binding.ivClaseIcono.setImageResource(R.drawable.guerrero_mentalista_icono)
                "NOVEL" -> binding.ivClaseIcono.setImageResource(R.drawable.novel_icono)
            }
        }
    }

    class PersonajeDiffCallback : DiffUtil.ItemCallback<Personaje>() {
        override fun areItemsTheSame(oldItem: Personaje, newItem: Personaje): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Personaje, newItem: Personaje): Boolean {
            return oldItem == newItem
        }
    }
}