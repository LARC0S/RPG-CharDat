package ies.quevedo.chardat.framework.fragmentsAddPersonaje

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
import ies.quevedo.chardat.databinding.FragmentAddPersonaje3Binding
import ies.quevedo.chardat.domain.model.Personaje
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*

@AndroidEntryPoint
class AddPersonajeFragmentValues : Fragment() {

    private val viewModel by viewModels<AddPersonajeViewModel>()
    private var _binding: FragmentAddPersonaje3Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentAddPersonaje3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            btCancelar.setOnClickListener {
                activity?.onBackPressed()
                findNavController().popBackStack(R.id.addPersonajeFragment1, true)
                findNavController().popBackStack(R.id.addPersonajeFragment2, true)
            }
            btCrear.setOnClickListener {
                if (faltaAlgunDato()) {
                    Toast.makeText(context, "Falta algun dato", Toast.LENGTH_SHORT).show()
                } else {
                    val personajeCreado = buildPersonaje()
                    insertPersonajeAndGoBack(personajeCreado)
                }
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.clear()
    }

    private fun insertPersonajeAndGoBack(personajeCreado: Personaje) {
        viewModel.handleEvent(AddPersonajeContract.Event.PostPersonaje(personajeCreado))
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { value ->
                if (value.personaje != null) {
                    findNavController().navigate(
                        R.id.action_addPersonajeFragment3_to_RVPersonajeFragment
                    )
                    findNavController().popBackStack(R.id.addPersonajeFragment1, true)
                    findNavController().popBackStack(R.id.addPersonajeFragment2, true)
                    findNavController().popBackStack(R.id.addPersonajeFragment3, true)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiError.collect {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun FragmentAddPersonaje3Binding.faltaAlgunDato() =
        etNivel.text.isNullOrBlank() ||
                etHabilidadDeAtaque.text.isNullOrBlank() ||
                etEsquiva.text.isNullOrBlank() ||
                etParada.text.isNullOrBlank() ||
                etArmadura.text.isNullOrBlank() ||
                etTurno.text.isNullOrBlank() ||
                etRF.text.isNullOrBlank() ||
                etRM.text.isNullOrBlank() ||
                etRP.text.isNullOrBlank() ||
                etPuntosHP.text.isNullOrBlank() ||
                etStamina.text.isNullOrBlank()

    private fun FragmentAddPersonaje3Binding.buildPersonaje(): Personaje {
        val nombrePersonaje = arguments?.getString("nombre") ?: ""
        val clasePersonaje = arguments?.getString("clase") ?: ""
        val descripcionPersonaje = arguments?.getString("descripcion")
        val agilidadPersonaje = arguments?.getInt("agilidad")
        val constitucionPersonaje = arguments?.getInt("constitucion")
        val destrezaPersonaje = arguments?.getInt("destreza")
        val fuerzaPersonaje = arguments?.getInt("fuerza")
        val inteligenciaPersonaje = arguments?.getInt("inteligencia")
        val percepcionPersonaje = arguments?.getInt("percepcion")
        val poderPersonaje = arguments?.getInt("poder")
        val voluntadPersonaje = arguments?.getInt("voluntad")
        val nivelPersonaje = etNivel.text.toString().toInt()
        val habilidadDeAtaquePersonaje = etHabilidadDeAtaque.text.toString().toInt()
        val esquivaPersonaje = etEsquiva.text.toString().toInt()
        val paradaPersonaje = etParada.text.toString().toInt()
        val armaduraPersonaje = etArmadura.text.toString().toInt()
        val turnoPersonaje = etTurno.text.toString().toInt()
        val rfPersonaje = etRF.text.toString().toInt()
        val rmPersonaje = etRM.text.toString().toInt()
        val rpPersonaje = etRP.text.toString().toInt()
        val hpPersonaje = etPuntosHP.text.toString().toInt()
        val staminaPersonaje = etStamina.text.toString().toInt()
        return Personaje(
            0,
            nombrePersonaje,
            clasePersonaje,
            nivelPersonaje,
            descripcionPersonaje!!,
            hpPersonaje,
            hpPersonaje,
            staminaPersonaje,
            staminaPersonaje,
            habilidadDeAtaquePersonaje,
            esquivaPersonaje,
            paradaPersonaje,
            armaduraPersonaje,
            turnoPersonaje,
            agilidadPersonaje!!,
            constitucionPersonaje!!,
            destrezaPersonaje!!,
            fuerzaPersonaje!!,
            inteligenciaPersonaje!!,
            percepcionPersonaje!!,
            poderPersonaje!!,
            voluntadPersonaje!!,
            rfPersonaje,
            rmPersonaje,
            rpPersonaje,
            LocalDate.now().toString(),
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList()
        )
    }
}