package ies.quevedo.chardat.framework.fragmentListObjetos

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
import ies.quevedo.chardat.databinding.FragmentObjetosBinding
import ies.quevedo.chardat.domain.model.Objeto
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RVObjetoFragment : Fragment() {

    private val viewModel by viewModels<RVObjetoViewModel>()
    private lateinit var adapter: RVObjetoAdapter
    private var _binding: FragmentObjetosBinding? = null
    private val binding get() = _binding!!
    private var idPersonaje: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentObjetosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        idPersonaje = arguments?.getInt("idPersonaje") ?: 0
        val layoutManager = LinearLayoutManager(context)
        binding.rvObjetos.addItemDecoration(
            DividerItemDecoration(
                binding.rvObjetos.context,
                layoutManager.orientation
            )
        )
        adapter = RVObjetoAdapter(
            ::goObjectDetails
        )
        binding.rvObjetos.adapter = adapter
        pedirObjetosDelPersonaje()
        binding.fbtRegister.setOnClickListener {
            val action =
                RVObjetoFragmentDirections.actionRVObjetoFragmentToAddObjetoFragment(
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

    private fun pedirObjetosDelPersonaje() {
        viewModel.handleEvent(ObjetoListContract.Event.FetchObjetos(idPersonaje ?: 0))
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { value ->
                binding.loading.visibility = if (value.isLoading) View.VISIBLE else View.GONE
                if (value.listaObjetos != null) {
                    adapter.submitList(value.listaObjetos)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiError.collect {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun deleteObjeto(objeto: Objeto) {
        viewModel.handleEvent(ObjetoListContract.Event.DeleteObjeto(objeto.id))
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { value ->
                if (value.objetoBorrado != null) {
                    pedirObjetosDelPersonaje()
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiError.collect {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun recoverObjeto(objeto: Objeto) {
        viewModel.handleEvent(ObjetoListContract.Event.PostObjeto(objeto))
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { value ->
                if (value.objetoRecuperado != null) {
                    pedirObjetosDelPersonaje()
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
                ItemTouchHelper.SimpleCallback(
                    0,
                    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                ) {
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
                    val objeto = adapter.currentList[viewHolder.absoluteAdapterPosition]
                    deleteObjeto(objeto)
                    Snackbar.make(
                        binding.root,
                        "Se ha eliminado: ${objeto.name}",
                        Snackbar.LENGTH_LONG
                    ).setAction("Deshacer") {
                        viewModel.handleEvent(
                            ObjetoListContract.Event.FetchObjetos(
                                idPersonaje ?: 0
                            )
                        )
                        recoverObjeto(objeto)
                    }.show()
                }
            }).attachToRecyclerView(binding.rvObjetos)
        }
    }

    private fun goObjectDetails(position: Int) {
        val objeto = adapter.currentList[position]
        if (objeto != null) {
            val action =
                RVObjetoFragmentDirections.actionRVObjetoFragmentToObjetoFragment(
                    objeto.id,
                    idPersonaje ?: 0
                )
            findNavController().navigate(action)
        } else {
            Toast.makeText(context, "No se ha podido obtener el objeto", Toast.LENGTH_SHORT).show()
        }
    }
}