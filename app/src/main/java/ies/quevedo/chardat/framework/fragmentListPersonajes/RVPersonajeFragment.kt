package ies.quevedo.chardat.framework.fragmentListPersonajes

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
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
import ies.quevedo.chardat.databinding.FragmentPersonajesBinding
import ies.quevedo.chardat.domain.model.Personaje
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class RVPersonajeFragment : Fragment() {

    private val viewModel by viewModels<RVPersonajeViewModel>()
    private lateinit var adapter: RVPersonajeAdapter
    private var _binding: FragmentPersonajesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonajesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        binding.rvPersonajes.addItemDecoration(
            DividerItemDecoration(
                binding.rvPersonajes.context,
                layoutManager.orientation
            )
        )
        adapter = RVPersonajeAdapter(
            ::goMainMenu
        )
        binding.rvPersonajes.adapter = adapter
        pedirPersonajes()
        binding.fbtRegister.setOnClickListener {
            findNavController().navigate(R.id.action_RVPersonajeFragment_to_addPersonajeFragment1)
        }
        swipeToDelete()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_item, menu)
        val search = menu.findItem(R.id.filter)
        val searchView = search?.actionView as? SearchView
        searchView?.maxWidth = Int.MAX_VALUE
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(filtro: String): Boolean {
                return true
            }

            override fun onQueryTextChange(filtro: String): Boolean {
                return true
            }
        })
        searchView?.isSubmitButtonEnabled = true
    }

    private fun pedirPersonajes() {
        viewModel.handleEvent(PersonajeListContract.Event.FetchPersonajes)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { value ->
                binding.loading.visibility = if (value.isLoading) View.VISIBLE else View.GONE
                if (value.listaPersonajes != null) {
                    adapter.submitList(value.listaPersonajes)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiError.collect {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun recoverPersonaje(personaje: Personaje) {
        viewModel.handleEvent(PersonajeListContract.Event.PostPersonaje(personaje))
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { value ->
                if (value.personajeRecuperado != null) {
                    pedirPersonajes()
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiError.collect {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun deletePersonaje(personaje: Personaje) {
        viewModel.handleEvent(PersonajeListContract.Event.DeletePersonaje(personaje.id))
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { value ->
                if (value.personajeBorrado != null) {
                    pedirPersonajes()
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
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val personaje = adapter.currentList[viewHolder.absoluteAdapterPosition]
                    deletePersonaje(personaje)
                    Snackbar.make(
                        binding.root,
                        "Se ha eliminado: ${personaje.name.uppercase(Locale.getDefault())}",
                        Snackbar.LENGTH_LONG
                    ).setAction("Deshacer") {
                        recoverPersonaje(personaje)
                    }.show()
                }
            }).attachToRecyclerView(binding.rvPersonajes)
        }
    }

    private fun goMainMenu(position: Int) {
        val personaje = adapter.currentList[position]
        if (personaje != null) {
            val action = RVPersonajeFragmentDirections
                .actionRVPersonajeFragmentToMainMenuFragment(personaje.id)
            findNavController().navigate(action)
        } else {
            Toast.makeText(context, "No se ha podido obtener el personaje", Toast.LENGTH_SHORT)
                .show()
        }
    }
}