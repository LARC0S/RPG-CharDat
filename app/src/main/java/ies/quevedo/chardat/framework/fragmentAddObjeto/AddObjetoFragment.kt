package ies.quevedo.chardat.framework.fragmentAddObjeto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ies.quevedo.chardat.R
import ies.quevedo.chardat.databinding.FragmentAddObjetoBinding
import ies.quevedo.chardat.domain.model.Objeto
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class AddObjetoFragment : Fragment() {

    private val viewModel by viewModels<AddObjetoViewModel>()
    private var _binding: FragmentAddObjetoBinding? = null
    private val binding get() = _binding!!
    private var idPersonaje: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentAddObjetoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            idPersonaje = arguments?.getInt("idPersonaje") ?: 0
            btCancelar.setOnClickListener {
                activity?.onBackPressed()
                findNavController().popBackStack(R.id.addObjetoFragment, true)
            }
            btCrear.setOnClickListener {
                if (faltaAlgunDato()) {
                    Toast.makeText(context, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
                } else {
                    val objetoCreado = buildObjeto()
                    insertObjetoAndGoBack(objetoCreado)
                }
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.clear()
    }

    private fun insertObjetoAndGoBack(objetoCreado: Objeto) {
        viewModel.handleEvent(AddObjetoContract.Event.PostObjeto(objetoCreado))
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { value ->
                if (value.objeto != null) {
                    findNavController().navigate(R.id.action_addObjetoFragment_to_RVObjetoFragment)
                    findNavController().popBackStack(R.id.addObjetoFragment, true)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiError.collect {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun FragmentAddObjetoBinding.buildObjeto(): Objeto {
        val nombreObjeto = etNombreObjeto.text.toString().uppercase(Locale.getDefault())
        val descripcionObjeto = etDescripcion.text.toString()
        val valorObjeto = etValor.text.toString().toInt()
        val cantidadObjeto = etCantidad.text.toString().toInt()
        val pesoObjeto = etPeso.text.toString().toDouble()
        return Objeto(
            0,
            nombreObjeto,
            valorObjeto,
            pesoObjeto,
            cantidadObjeto,
            descripcionObjeto,
            idPersonaje ?: 0
        )
    }

    private fun FragmentAddObjetoBinding.faltaAlgunDato() =
        etNombreObjeto.text.isNullOrEmpty() ||
                etDescripcion.text.isNullOrEmpty() ||
                etValor.text.isNullOrEmpty() ||
                etCantidad.text.isNullOrEmpty() ||
                etPeso.text.isNullOrEmpty()
}