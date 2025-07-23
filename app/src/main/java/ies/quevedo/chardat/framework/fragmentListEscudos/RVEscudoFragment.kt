package ies.quevedo.chardat.framework.fragmentListEscudos

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ies.quevedo.chardat.R
import ies.quevedo.chardat.databinding.FragmentEscudosBinding
import ies.quevedo.chardat.domain.model.Escudo
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RVEscudoFragment : Fragment() {

    private val viewModel by viewModels<RVEscudoViewModel>()
    private lateinit var adapter: RVEscudoAdapter
    private var _binding: FragmentEscudosBinding? = null
    private val binding get() = _binding!!
    private var idPersonaje: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEscudosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        idPersonaje = arguments?.getInt("idPersonaje") ?: 0
        val layoutManager = LinearLayoutManager(context)
        binding.rvEscudos.addItemDecoration(
            DividerItemDecoration(
                binding.rvEscudos.context,
                layoutManager.orientation
            )
        )
        adapter = RVEscudoAdapter(
            ::goShieldDetails
        )
        binding.rvEscudos.adapter = adapter
        pedirEscudosDelPersonaje()
        binding.fbtRegister.setOnClickListener {
            val action = RVEscudoFragmentDirections.actionRVEscudoFragmentToAddEscudoFragment(
                idPersonaje ?: 0
            )
            findNavController().navigate(action)
        }
        swipeToDelete()
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu, inflater: android.view.MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_item, menu)
        val actionSearch = menu.findItem(R.id.filter).actionView as SearchView
        actionSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun pedirEscudosDelPersonaje() {
        viewModel.handleEvent(EscudoListContract.Event.FetchEscudos(idPersonaje ?: 0))
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { value ->
                binding.loading.visibility = if (value.isLoading) View.VISIBLE else View.GONE
                if (value.listaEscudos != null) {
                    adapter.submitList(value.listaEscudos)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiError.collect {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun recoverEscudo(escudo: Escudo) {
        viewModel.handleEvent(EscudoListContract.Event.PostEscudo(escudo))
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { value ->
                if (value.escudoRecuperado != null) {
                    pedirEscudosDelPersonaje()
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiError.collect {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun deleteEscudo(escudo: Escudo) {
        viewModel.handleEvent(EscudoListContract.Event.DeleteEscudo(escudo.id))
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { value ->
                if (value.escudoBorrado != null) {
                    pedirEscudosDelPersonaje()
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiError.collect {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun swipeToDelete() {
        binding.apply {
            ItemTouchHelper(object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                @SuppressLint("NotifyDataSetChanged")
                override fun onSwiped(
                    viewHolder: RecyclerView.ViewHolder,
                    direction: Int
                ) {
                    val escudo = adapter.currentList[viewHolder.absoluteAdapterPosition]
                    deleteEscudo(escudo)
                    Snackbar.make(
                        binding.root,
                        "Se ha eliminado: ${escudo.name}",
                        Snackbar.LENGTH_LONG
                    ).setAction("Deshacer") {
                        recoverEscudo(escudo)
                    }.show()
                }
            }).attachToRecyclerView(binding.rvEscudos)
        }
    }

    private fun goShieldDetails(position: Int) {
        val escudo = adapter.currentList[position]
        if (escudo != null) {
            val action =
                RVEscudoFragmentDirections.actionRVEscudoFragmentToEscudoFragment(
                    escudo.id,
                    idPersonaje ?: 0
                )
            findNavController().navigate(action)
        } else {
            Toast.makeText(context, "No se ha podido obtener el escudo", Toast.LENGTH_SHORT).show()
        }
    }
}