package ies.quevedo.chardat.framework.fragmentShowArma

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
import ies.quevedo.chardat.databinding.FragmentArmaBinding
import ies.quevedo.chardat.domain.model.Arma
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShowArmaFragment : Fragment() {

    private val viewModel by viewModels<ShowArmaViewModel>()
    private var _binding: FragmentArmaBinding? = null
    private val binding get() = _binding!!
    private var idArma: Int? = null
    private var idPersonaje: Int? = null
    private var arma: Arma? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentArmaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        idArma = arguments?.getInt("idArma") ?: 0
        idPersonaje = arguments?.getInt("idPersonaje") ?: 0
        pedirArma()
        with(binding) {
            btCancelar.setOnClickListener {
                activity?.onBackPressed()
                findNavController().popBackStack(R.id.armaFragment, true)
            }
            btModificar.setOnClickListener {
                if (faltaAlgunDato()) {
                    Toast.makeText(requireContext(), "Rellena todos los campos", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    val armaActualizada = buildArmaActualizada()
                    updateArmaAndGoBack(armaActualizada)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        with(binding) {
            val listaDeArmas: Array<String> = listaDeArmas()
            val adapter1 = ArrayAdapter(requireContext(), R.layout.list_item, listaDeArmas)
            (etNombreArma as? AutoCompleteTextView)?.setAdapter(adapter1)
            val listaDeNumeros: Array<Int> = arrayOf(-10, -5, 0, 5, 10, 15, 20, 25, 30)
            val adapter2 = ArrayAdapter(requireContext(), R.layout.list_item, listaDeNumeros)
            (etCalidad as? AutoCompleteTextView?)?.setAdapter(adapter2)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.clear()
    }

    private fun updateArmaAndGoBack(armaActualizada: Arma?) {
        viewModel.handleEvent(ShowArmaContract.Event.PutArma(armaActualizada))
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { value ->
                if (value.armaActualizada != null) {
                    val action =
                        ShowArmaFragmentDirections.actionArmaFragmentToRVArmaFragment(
                            idPersonaje ?: 0
                        )
                    findNavController().navigate(action)
                    findNavController().popBackStack(R.id.armaFragment, true)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiError.collect {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun pedirArma() {
        viewModel.handleEvent(ShowArmaContract.Event.FetchArma(idArma ?: 0))
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { value ->
                if (value.arma != null) {
                    arma = value.arma
                    rellenarCamposDeArma(value.arma)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiError.collect {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun rellenarCamposDeArma(arma: Arma) {
        with(binding) {
            etNombreArma.setText(arma.name)
            etCalidad.setText(arma.quality.toString())
            etDescripcion.setText(arma.description)
            etTurno.setText(arma.turn.toString())
            etHabilidadDeAtaque.setText(arma.attackHability.toString())
            etDamage.setText(arma.damage.toString())
            etParada.setText(arma.parry.toString())
            etValor.setText(arma.value.toString())
            etPeso.setText(arma.weight.toString())
        }
    }

    private fun FragmentArmaBinding.buildArmaActualizada(): Arma? {
        arma?.name = etNombreArma.text.toString()
        arma?.quality = etCalidad.text.toString().toInt()
        arma?.description = etDescripcion.text.toString()
        arma?.turn = etTurno.text.toString().toInt()
        arma?.attackHability = etHabilidadDeAtaque.text.toString().toInt()
        arma?.damage = etDamage.text.toString().toInt()
        arma?.parry = etParada.text.toString().toInt()
        arma?.value = etValor.text.toString().toInt()
        arma?.weight = etPeso.text.toString().toDouble()
        return arma
    }

    private fun FragmentArmaBinding.faltaAlgunDato() =
        etNombreArma.text.isNullOrBlank() ||
                etCalidad.text.isNullOrBlank() ||
                etDescripcion.text.isNullOrBlank() ||
                etTurno.text.isNullOrBlank() ||
                etHabilidadDeAtaque.text.isNullOrBlank() ||
                etDamage.text.isNullOrBlank() ||
                etParada.text.isNullOrBlank() ||
                etValor.text.isNullOrBlank() ||
                etPeso.text.isNullOrBlank()

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