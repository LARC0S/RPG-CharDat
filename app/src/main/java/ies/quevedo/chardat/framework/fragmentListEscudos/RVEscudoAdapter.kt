package ies.quevedo.chardat.framework.fragmentListEscudos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ies.quevedo.chardat.R
import ies.quevedo.chardat.databinding.CardItemFragmentBinding
import ies.quevedo.chardat.domain.model.Escudo

class RVEscudoAdapter(
    private val goShieldDetails: (Int) -> Unit
) : ListAdapter<Escudo, RVEscudoAdapter.ItemViewHolder>(EscudoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.card_item_fragment, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        with(holder) {
            val item = getItem(position)
            bind(item, goShieldDetails)
        }
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = CardItemFragmentBinding.bind(itemView)
        fun bind(
            item: Escudo,
            goShieldDetails: (Int) -> Unit
        ) = with(binding) {
            tvName.text = item.name
            tvDescription.text = item.description
            cardPersonaje.setOnClickListener { goShieldDetails(absoluteAdapterPosition) }
        }
    }

    class EscudoDiffCallback : DiffUtil.ItemCallback<Escudo>() {
        override fun areItemsTheSame(oldItem: Escudo, newItem: Escudo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Escudo, newItem: Escudo): Boolean {
            return oldItem == newItem
        }
    }
}