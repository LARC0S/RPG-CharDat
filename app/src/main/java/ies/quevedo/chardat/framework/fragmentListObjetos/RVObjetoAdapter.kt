package ies.quevedo.chardat.framework.fragmentListObjetos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ies.quevedo.chardat.R
import ies.quevedo.chardat.databinding.CardItemFragmentBinding
import ies.quevedo.chardat.domain.model.Objeto

class RVObjetoAdapter(
    private val goObjectDetails: (Int) -> Unit
) : ListAdapter<Objeto, RVObjetoAdapter.ItemViewHolder>(ObjetoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.card_item_fragment, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        with(holder) {
            val item = getItem(position)
            bind(item, goObjectDetails)
        }
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = CardItemFragmentBinding.bind(itemView)
        fun bind(
            item: Objeto,
            goObjectDetails: (Int) -> Unit
        ) = with(binding) {
            tvName.text = item.name
            tvDescription.text = item.description
            cardPersonaje.setOnClickListener { goObjectDetails(absoluteAdapterPosition) }
        }
    }

    class ObjetoDiffCallback : DiffUtil.ItemCallback<Objeto>() {
        override fun areItemsTheSame(oldItem: Objeto, newItem: Objeto): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Objeto, newItem: Objeto): Boolean {
            return oldItem == newItem
        }
    }
}
