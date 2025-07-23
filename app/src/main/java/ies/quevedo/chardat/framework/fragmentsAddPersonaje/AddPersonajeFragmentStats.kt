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
import dagger.hilt.android.AndroidEntryPoint
import ies.quevedo.chardat.R
import ies.quevedo.chardat.databinding.FragmentAddPersonaje2Binding

@AndroidEntryPoint
class AddPersonajeFragmentStats : Fragment() {

    private var _binding: FragmentAddPersonaje2Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentAddPersonaje2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            rellenarNumeros()
            btCancelar.setOnClickListener {
                activity?.onBackPressed()
                findNavController().popBackStack(R.id.addPersonajeFragment1, true)
            }
            btSiguiente.setOnClickListener {
                if (faltaAlgunDato()) {
                    Toast.makeText(context, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
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

    private fun FragmentAddPersonaje2Binding.navigateNextWithData() {
        val clase = arguments?.getString("clase") ?: ""
        val nombre = arguments?.getString("nombre") ?: ""
        val descripcion = arguments?.getString("descripcion") ?: ""
        val agilidad = etAGI.text.toString().toInt()
        val constitucion = etCON.text.toString().toInt()
        val destreza = etDES.text.toString().toInt()
        val fuerza = etFUE.text.toString().toInt()
        val inteligencia = etINT.text.toString().toInt()
        val percepcion = etPER.text.toString().toInt()
        val poder = etPOD.text.toString().toInt()
        val voluntad = etVOL.text.toString().toInt()
        val action =
            AddPersonajeFragmentStatsDirections.actionAddPersonajeFragment2ToAddPersonajeFragment3(
                clase,
                nombre,
                descripcion,
                agilidad,
                constitucion,
                destreza,
                fuerza,
                inteligencia,
                percepcion,
                poder,
                voluntad
            )
        findNavController().navigate(action)
    }

    private fun FragmentAddPersonaje2Binding.rellenarNumeros() {
        val listaDeNumeros: Array<Int> = arrayOf(10, 9, 8, 7, 6, 5, 4, 3, 2, 1)
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, listaDeNumeros)
        (etAGI as? AutoCompleteTextView)?.setAdapter(adapter)
        (etCON as? AutoCompleteTextView)?.setAdapter(adapter)
        (etDES as? AutoCompleteTextView)?.setAdapter(adapter)
        (etFUE as? AutoCompleteTextView)?.setAdapter(adapter)
        (etINT as? AutoCompleteTextView)?.setAdapter(adapter)
        (etPER as? AutoCompleteTextView)?.setAdapter(adapter)
        (etPOD as? AutoCompleteTextView)?.setAdapter(adapter)
        (etVOL as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun FragmentAddPersonaje2Binding.faltaAlgunDato() =
        etAGI.text.isNullOrBlank() ||
                etCON.text.isNullOrBlank() ||
                etDES.text.isNullOrBlank() ||
                etFUE.text.isNullOrBlank() ||
                etINT.text.isNullOrBlank() ||
                etPER.text.isNullOrBlank() ||
                etPOD.text.isNullOrBlank() ||
                etVOL.text.isNullOrBlank()
}