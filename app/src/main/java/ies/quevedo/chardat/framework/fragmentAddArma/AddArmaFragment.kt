package ies.quevedo.chardat.framework.fragmentAddArma

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
import ies.quevedo.chardat.databinding.FragmentAddArmaBinding
import ies.quevedo.chardat.domain.model.Arma
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddArmaFragment : Fragment() {

    private val viewModel by viewModels<AddArmaViewModel>()
    private var _binding: FragmentAddArmaBinding? = null
    private val binding get() = _binding!!
    private var idPersonaje: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentAddArmaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        idPersonaje = arguments?.getInt("idPersonaje") ?: 0
        with(binding) {
            rellenarCalidad()
            rellenarArmas()
            btCancelar.setOnClickListener {
                activity?.onBackPressed()
                findNavController().popBackStack(R.id.armaFragment, true)
            }
            btCrear.setOnClickListener {
                if (faltaAlgunDato()) {
                    Toast.makeText(context, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
                } else {
                    val armaCreada = buildArma()
                    insertArmaAndGoBack(armaCreada)
                }
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.clear()
    }

    private fun insertArmaAndGoBack(armaCreada: Arma) {
        viewModel.handleEvent(AddArmaContract.Event.PostArma(armaCreada))
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { value ->
                if (value.arma != null) {
                    findNavController().navigate(
                        R.id.action_addArmaFragment_to_RVArmaFragment
                    )
                    findNavController().popBackStack(R.id.addArmaFragment, true)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiError.collect {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun FragmentAddArmaBinding.buildArma(): Arma {
        val nombreArma = etNombreArma.text.toString()
        val valorArma = etValor.text.toString().toInt()
        val pesoArma = etPeso.text.toString().toDouble()
        val calidadArma = etCalidad.text.toString().toInt()
        val turnoArma = etTurno.text.toString().toInt()
        val habilidadAtaqueArma = etHabilidadDeAtaque.text.toString().toInt()
        val damageArma = etDamage.text.toString().toInt()
        val paradaArma = etParada.text.toString().toInt()
        val descripcionArma = etDescripcion.text.toString()
        return Arma(
            0,
            nombreArma,
            valorArma,
            pesoArma,
            calidadArma,
            turnoArma,
            habilidadAtaqueArma,
            damageArma,
            paradaArma,
            descripcionArma,
            idPersonaje ?: 0
        )
    }

    private fun FragmentAddArmaBinding.faltaAlgunDato() =
        etNombreArma.text.isNullOrBlank() ||
                etValor.text.isNullOrBlank() ||
                etPeso.text.isNullOrBlank() ||
                etCalidad.text.isNullOrBlank() ||
                etTurno.text.isNullOrBlank() ||
                etHabilidadDeAtaque.text.isNullOrBlank() ||
                etDamage.text.isNullOrBlank() ||
                etParada.text.isNullOrBlank() ||
                etDescripcion.text.isNullOrBlank()

    private fun FragmentAddArmaBinding.rellenarArmas() {
        val listaDeArmas: Array<String> = listaDeArmas()
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, listaDeArmas)
        (etNombreArma as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun FragmentAddArmaBinding.rellenarCalidad() {
        val listaDeNumeros: Array<Int> = arrayOf(-10, -5, 0, 5, 10, 15, 20, 25, 30)
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, listaDeNumeros)
        (etCalidad as? AutoCompleteTextView?)?.setAdapter(adapter)
    }

    private fun listaDeArmas(): Array<String> {
        return listOf(
            "ALABARDA",
            "ARCO COMPUESTO",
            "ARCO CORTO",
            "ARCO LARGO",
            "ARPÓN",
            "BALLESTA",
            "BALLESTA DE MANO",
            "BALLESTA DE REPETICIÓN",
            "BALLESTA PESADA",
            "CADENA",
            "CERBATANA",
            "CESTUS",
            "CIMITARRA",
            "DAGA",
            "DAGA DE PARADA",
            "ESPADA ANCHA",
            "ESPADA BASTARDA",
            "ESPADA CORTA",
            "ESPADA LARGA",
            "ESTILETE",
            "ESTOQUE",
            "FLAGELO",
            "FLORETE",
            "FUSIL",
            "GARFIO",
            "GARROTE",
            "GRAN MARTILLO DE GUERRA",
            "GUADAÑA",
            "HACHA A DOS MANOS",
            "HACHA DE GUERRA",
            "HACHA DE MANO",
            "HONDA",
            "JABALINA",
            "LANZA",
            "LANZA DE CABALLERÍA",
            "LÁTIGO",
            "LAZO",
            "MANDOBLE",
            "MANGUAL",
            "MARTILLO DE GUERRA",
            "MAYAL",
            "MAZA",
            "MAZA PESADA",
            "PISTOLA",
            "RED DE GLADIADOR",
            "SABLE",
            "TRIDENTE",
            "VARA"
        ).toTypedArray()
    }
}