package ies.quevedo.chardat.framework.fragmentsAddPersonaje

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ies.quevedo.chardat.R
import ies.quevedo.chardat.databinding.FragmentAddPersonaje1Binding

class AddPersonajeFragmentText : Fragment() {

    private var _binding: FragmentAddPersonaje1Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentAddPersonaje1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            val listaDeClases: Array<String> = listaDeClases()
            val adapter = ArrayAdapter(requireContext(), R.layout.list_item, listaDeClases)
            (etClases as? AutoCompleteTextView)?.setAdapter(adapter)
            btCancelar.setOnClickListener {
                activity?.onBackPressed()
            }
            btSiguiente.setOnClickListener {
                if (faltaAlgunDato()) {
                    Toast.makeText(requireContext(), "Rellena todos los campos", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    navigateNextWithData()
                }
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.clear()
    }

    private fun FragmentAddPersonaje1Binding.navigateNextWithData() {
        val clase = etClases.text.toString()
        val nombre = etNombrePersonaje.text.toString()
        val descripcion = etDescripcion.text.toString()
        val action =
            AddPersonajeFragmentTextDirections.actionAddPersonajeFragment1ToAddPersonajeFragment2(
                clase,
                nombre,
                descripcion
            )
        findNavController().navigate(action)
    }

    private fun FragmentAddPersonaje1Binding.faltaAlgunDato() =
        etNombrePersonaje.text.isNullOrBlank() ||
                etClases.text.isNullOrBlank() ||
                etDescripcion.text.isNullOrBlank()

    private fun listaDeClases(): Array<String> {
        return listOf(
            "GUERRERO",
            "GUERRERO ACRÓBATA",
            "PALADÍN",
            "PALADÍN OSCURO",
            "MAESTRO DE ARMAS",
            "TECNICISTA",
            "TAO",
            "EXPLORADOR",
            "SOMBRA",
            "LADRÓN",
            "ASESINO",
            "HECHICERO",
            "WARLOCK",
            "ILUSIONISTA",
            "HECHICERO MENTALISTA",
            "CONJURADOR",
            "GUERRERO CONJURADOR",
            "MENTALISTA",
            "GUERRERO MENTALISTA",
            "NOVEL"
        ).toTypedArray()
    }
}