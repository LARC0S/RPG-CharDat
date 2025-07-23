package ies.quevedo.chardat.framework.fragmentAddEscudo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ies.quevedo.chardat.R
import ies.quevedo.chardat.databinding.FragmentAddEscudoBinding
import ies.quevedo.chardat.domain.model.Escudo
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddEscudoFragment : Fragment() {

    private val viewModel by viewModels<AddEscudoViewModel>()
    private var _binding: FragmentAddEscudoBinding? = null
    private val binding get() = _binding!!
    private var idPersonaje: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentAddEscudoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        idPersonaje = arguments?.getInt("idPersonaje") ?: 0
        with(binding) {
            rellenarCalidad()
            rellenarEscudos()
            btCancelar.setOnClickListener {
                activity?.onBackPressed()
                findNavController().popBackStack(R.id.addEscudoFragment, true)
            }
            btCrear.setOnClickListener {
                if (faltaAlgunDato()) {
                    Toast.makeText(context, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
                } else {
                    val escudoCreado = buildEscudo()
                    insertEscudoAndGoBack(escudoCreado)
                }
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.clear()
    }

    private fun insertEscudoAndGoBack(escudoCreado: Escudo) {
        viewModel.handleEvent(AddEscudoContract.Event.PostEscudo(escudoCreado))
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { value ->
                if (value.escudo != null) {
                    findNavController().navigate(R.id.action_addEscudoFragment_to_RVEscudoFragment)
                    findNavController().popBackStack(R.id.addEscudoFragment, true)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiError.collect {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun FragmentAddEscudoBinding.buildEscudo(): Escudo {
        val nombreEscudo = etNombreEscudo.text.toString()
        val calidadEscudo = etCalidad.text.toString().toInt()
        val descripcionEscudo = etDescripcion.text.toString()
        val habilidadDeAtaqueDeEscudo = etHabilidadDeAtaque.text.toString().toInt()
        val damageEscudo = etDamage.text.toString().toInt()
        val paradaEscudo = etParada.text.toString().toInt()
        val valorEscudo = etValor.text.toString().toInt()
        val pesoEscudo = etPeso.text.toString().toDouble()
        return Escudo(
            0,
            nombreEscudo,
            valorEscudo,
            pesoEscudo,
            calidadEscudo,
            habilidadDeAtaqueDeEscudo,
            damageEscudo,
            paradaEscudo,
            descripcionEscudo,
            idPersonaje ?: 0
        )
    }

    private fun FragmentAddEscudoBinding.faltaAlgunDato() =
        etNombreEscudo.text.isNullOrBlank() ||
                etCalidad.text.isNullOrBlank() ||
                etDescripcion.text.isNullOrBlank() ||
                etHabilidadDeAtaque.text.isNullOrBlank() ||
                etDamage.text.isNullOrBlank() ||
                etParada.text.isNullOrBlank() ||
                etValor.text.isNullOrBlank() ||
                etPeso.text.isNullOrBlank()

    private fun FragmentAddEscudoBinding.rellenarEscudos() {
        val listaDeArmaduras: Array<String> = listaDeEscudos()
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, listaDeArmaduras)
        (etNombreEscudo as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun FragmentAddEscudoBinding.rellenarCalidad() {
        val listaDeNumeros: Array<Int> = arrayOf(-10, -5, 0, 5, 10, 15, 20, 25, 30)
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, listaDeNumeros)
        (etCalidad as? AutoCompleteTextView?)?.setAdapter(adapter)
    }

    private fun listaDeEscudos(): Array<String> {
        return listOf(
            "ESCUDO",
            "ESCUDO CORPORAL",
            "RODELA"
        ).toTypedArray()
    }
}