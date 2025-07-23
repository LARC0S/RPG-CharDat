package ies.quevedo.chardat.framework.fragmentAddArmadura

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
import ies.quevedo.chardat.R
import ies.quevedo.chardat.databinding.FragmentAddArmaduraBinding
import ies.quevedo.chardat.domain.model.Armadura
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AddArmaduraFragment : Fragment() {

    private val viewModel by viewModels<AddArmaduraViewModel>()
    private var _binding: FragmentAddArmaduraBinding? = null
    private val binding get() = _binding!!
    private var idPersonaje: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentAddArmaduraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        idPersonaje = arguments?.getInt("idPersonaje") ?: 0
        with(binding) {
            rellenarCalidad()
            rellenarArmaduras()
            btCancelar.setOnClickListener {
                activity?.onBackPressed()
                findNavController().popBackStack(R.id.armaduraFragment, true)
            }
            btCrear.setOnClickListener {
                if (faltaAlgunDato()) {
                    Toast.makeText(context, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
                } else {
                    val armaduraCreada = buildArmadura()
                    insertArmaduraAndGoBack(armaduraCreada)
                }
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.clear()
    }

    private fun insertArmaduraAndGoBack(armaduraCreada: Armadura) {
        viewModel.handleEvent(AddArmaduraContract.Event.PostArmadura(armaduraCreada))
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { value ->
                if (value.armadura != null) {
                    findNavController().navigate(
                        R.id.action_addArmaduraFragment_to_RVArmaduraFragment
                    )
                    findNavController().popBackStack(R.id.addArmaduraFragment, true)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiError.collect {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun FragmentAddArmaduraBinding.buildArmadura(): Armadura {
        val nombreArmadura = etNombreArmadura.text.toString()
        val calidadArma = etCalidad.text.toString().toInt()
        val descripcionArmadura = etDescripcion.text.toString()
        val fil = etFIL.text.toString().toInt()
        val con = etCON.text.toString().toInt()
        val pen = etPEN.text.toString().toInt()
        val cal = etCAL.text.toString().toInt()
        val ele = etELE.text.toString().toInt()
        val fri = etFRI.text.toString().toInt()
        val ene = etENE.text.toString().toInt()
        val llevarArmadura = etArmadura.text.toString().toInt()
        val valorArma = etValor.text.toString().toInt()
        val pesoArma = etPeso.text.toString().toDouble()
        return Armadura(
            0,
            nombreArmadura,
            valorArma,
            pesoArma,
            calidadArma,
            llevarArmadura,
            fil,
            con,
            pen,
            cal,
            ele,
            fri,
            ene,
            descripcionArmadura,
            idPersonaje ?: 0
        )
    }

    private fun FragmentAddArmaduraBinding.faltaAlgunDato() =
        etNombreArmadura.text.isNullOrBlank() ||
                etCalidad.text.isNullOrBlank() ||
                etDescripcion.text.isNullOrBlank() ||
                etFIL.text.isNullOrBlank() ||
                etCON.text.isNullOrBlank() ||
                etPEN.text.isNullOrBlank() ||
                etCAL.text.isNullOrBlank() ||
                etELE.text.isNullOrBlank() ||
                etFRI.text.isNullOrBlank() ||
                etENE.text.isNullOrBlank() ||
                etArmadura.text.isNullOrBlank() ||
                etValor.text.isNullOrBlank() ||
                etPeso.text.isNullOrBlank()

    private fun FragmentAddArmaduraBinding.rellenarArmaduras() {
        val listaDeArmaduras: Array<String> = listaDeArmaduras()
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, listaDeArmaduras)
        (etNombreArmadura as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun FragmentAddArmaduraBinding.rellenarCalidad() {
        val listaDeNumeros: Array<Int> = arrayOf(-10, -5, 0, 5, 10, 15, 20, 25, 30)
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, listaDeNumeros)
        (etCalidad as? AutoCompleteTextView?)?.setAdapter(adapter)
    }

    private fun listaDeArmaduras(): Array<String> {
        return listOf(
            "ACOLCHADA",
            "ANILLAS",
            "COMPLETA",
            "COMPLETA DE CUERO",
            "COMPLETA PESADA",
            "COTA DE CUERO",
            "CUERO ENDURECIDO",
            "CUERO TACHONADO",
            "DE CAMPAÑA PESADA",
            "ESCAMAS",
            "GABARDINA",
            "MALLAS",
            "PETO",
            "PIEL",
            "PIEZAS",
            "PLACAS",
            "SEMICOMPLETA"
        ).toTypedArray()
    }
}