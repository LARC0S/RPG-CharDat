package ies.quevedo.chardat.framework.fragmentShowEscudo

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
import ies.quevedo.chardat.databinding.FragmentEscudoBinding
import ies.quevedo.chardat.domain.model.Escudo
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShowEscudoFragment : Fragment() {

    private val viewModel by viewModels<ShowEscudoViewModel>()
    private var _binding: FragmentEscudoBinding? = null
    private val binding get() = _binding!!
    private var idEscudo: Int? = null
    private var idPersonaje: Int? = null
    private var escudo: Escudo? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentEscudoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        idEscudo = arguments?.getInt("idEscudo") ?: 0
        idPersonaje = arguments?.getInt("idPersonaje") ?: 0
        pedirEscudo()
        with(binding) {
            btCancelar.setOnClickListener {
                activity?.onBackPressed()
                findNavController().popBackStack(R.id.escudoFragment, true)
            }
            btModificar.setOnClickListener {
                if (faltaAlgunDato()) {
                    Toast.makeText(requireContext(), "Rellena todos los campos", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    val escudoActualizado = buildEscudoActualizado()
                    updateEscudoAndGoBack(escudoActualizado)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        with(binding) {
            val listaDeArmaduras: Array<String> = listaDeEscudos()
            val adapter1 = ArrayAdapter(requireContext(), R.layout.list_item, listaDeArmaduras)
            (etNombreEscudo as? AutoCompleteTextView)?.setAdapter(adapter1)
            val listaDeNumeros: Array<Int> = arrayOf(-10, -5, 0, 5, 10, 15, 20, 25, 30)
            val adapter2 = ArrayAdapter(requireContext(), R.layout.list_item, listaDeNumeros)
            (etCalidad as? AutoCompleteTextView?)?.setAdapter(adapter2)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.clear()
    }

    private fun pedirEscudo() {
        viewModel.handleEvent(ShowEscudoContract.Event.FetchEscudo(idEscudo ?: 0))
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { value ->
                if (value.escudo != null) {
                    escudo = value.escudo
                    rellenarCamposDeEscudo()
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiError.collect {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updateEscudoAndGoBack(escudoActualizado: Escudo?) {
        viewModel.handleEvent(ShowEscudoContract.Event.PutEscudo(escudoActualizado))
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { value ->
                if (value.escudo != null) {
                    val action =
                        ShowEscudoFragmentDirections.actionEscudoFragmentToRVEscudoFragment(
                            idPersonaje ?: 0
                        )
                    findNavController().navigate(action)
                    findNavController().popBackStack(R.id.escudoFragment, true)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiError.collect {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun rellenarCamposDeEscudo() {
        with(binding) {
            etNombreEscudo.setText(escudo?.name)
            etCalidad.setText(escudo?.quality.toString())
            etDescripcion.setText(escudo?.description)
            etHabilidadDeAtaque.setText(escudo?.attackHability.toString())
            etDamage.setText(escudo?.damage.toString())
            etParada.setText(escudo?.parry.toString())
            etValor.setText(escudo?.value.toString())
            etPeso.setText(escudo?.weight.toString())
        }
    }

    private fun FragmentEscudoBinding.buildEscudoActualizado(): Escudo? {
        escudo?.name = etNombreEscudo.text.toString()
        escudo?.quality = etCalidad.text.toString().toInt()
        escudo?.description = etDescripcion.text.toString()
        escudo?.attackHability = etHabilidadDeAtaque.text.toString().toInt()
        escudo?.damage = etDamage.text.toString().toInt()
        escudo?.parry = etParada.text.toString().toInt()
        escudo?.value = etValor.text.toString().toInt()
        escudo?.weight = etPeso.text.toString().toDouble()
        return escudo
    }

    private fun FragmentEscudoBinding.faltaAlgunDato() =
        etNombreEscudo.text.isNullOrBlank() ||
                etCalidad.text.isNullOrBlank() ||
                etDescripcion.text.isNullOrBlank() ||
                etHabilidadDeAtaque.text.isNullOrBlank() ||
                etDamage.text.isNullOrBlank() ||
                etParada.text.isNullOrBlank() ||
                etValor.text.isNullOrBlank() ||
                etPeso.text.isNullOrBlank()

    private fun listaDeEscudos(): Array<String> {
        return listOf(
            "ESCUDO",
            "ESCUDO CORPORAL",
            "RODELA"
        ).toTypedArray()
    }
}