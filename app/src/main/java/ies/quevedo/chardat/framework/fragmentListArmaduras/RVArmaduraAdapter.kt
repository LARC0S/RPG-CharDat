package ies.quevedo.chardat.framework.fragmentListArmaduras

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ies.quevedo.chardat.R
import ies.quevedo.chardat.databinding.CardItemFragmentBinding
import ies.quevedo.chardat.domain.model.Armadura

class RVArmaduraAdapter(
    private val goArmorDetails: (Int) -> Unit
) : ListAdapter<Armadura, RVArmaduraAdapter.ItemViewHolder>(ArmaduraDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.card_item_fragment, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        with(holder) {
            val item = getItem(position)
            bind(item, goArmorDetails)
        }
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = CardItemFragmentBinding.bind(itemView)
        fun bind(
            item: Armadura,
            goArmorDetails: (Int) -> Unit
        ) = with(binding) {
            tvName.text = item.name
            tvDescription.text = item.description
            cardPersonaje.setOnClickListener { goArmorDetails(absoluteAdapterPosition) }
        }
    }

    class ArmaduraDiffCallback : DiffUtil.ItemCallback<Armadura>() {
        override fun areItemsTheSame(oldItem: Armadura, newItem: Armadura): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Armadura, newItem: Armadura): Boolean {
            return oldItem == newItem
        }
    }
}