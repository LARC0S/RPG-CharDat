package ies.quevedo.chardat.framework.fragmentShowArmadura

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
import ies.quevedo.chardat.databinding.FragmentArmaduraBinding
import ies.quevedo.chardat.domain.model.Armadura
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShowArmaduraFragment : Fragment() {

    private val viewModel by viewModels<ShowArmaduraViewModel>()
    private var _binding: FragmentArmaduraBinding? = null
    private val binding get() = _binding!!
    private var idArmadura: Int? = null
    private var idPersonaje: Int? = null
    private var armadura: Armadura? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentArmaduraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        idArmadura = arguments?.getInt("idArmadura") ?: 0
        idPersonaje = arguments?.getInt("idPersonaje") ?: 0
        pedirArmadura()
        with(binding) {
            btCancelar.setOnClickListener {
                activity?.onBackPressed()
                findNavController().popBackStack(R.id.armaduraFragment, true)
            }
            btModificar.setOnClickListener {
                if (faltaAlgunDato()) {
                    Toast.makeText(requireContext(), "Rellena todos los campos", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    val armaduraActualizada = buildArmaduraActualizada()
                    updateArmaduraAndGoBack(armaduraActualizada)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        with(binding) {
            val listaDeArmaduras: Array<String> = listaDeArmaduras()
            val adapter1 = ArrayAdapter(requireContext(), R.layout.list_item, listaDeArmaduras)
            (etNombreArmadura as? AutoCompleteTextView)?.setAdapter(adapter1)
            val listaDeNumeros: Array<Int> = arrayOf(-10, -5, 0, 5, 10, 15, 20, 25, 30)
            val adapter2 = ArrayAdapter(requireContext(), R.layout.list_item, listaDeNumeros)
            (etCalidad as? AutoCompleteTextView?)?.setAdapter(adapter2)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.clear()
    }

    private fun pedirArmadura() {
        viewModel.handleEvent(ShowArmaduraContract.Event.FetchArmadura(idArmadura ?: 0))
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { value ->
                if (value.armadura != null) {
                    armadura = value.armadura
                    rellenarCamposDeArmadura()
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiError.collect {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updateArmaduraAndGoBack(armaduraActualizada: Armadura?) {
        viewModel.handleEvent(ShowArmaduraContract.Event.PutArmadura(armaduraActualizada))
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { value ->
                if (value.armaduraActualizada != null) {
                    val action =
                        ShowArmaduraFragmentDirections.actionArmaduraFragmentToRVArmaduraFragment(
                            idPersonaje ?: 0
                        )
                    findNavController().navigate(action)
                    findNavController().popBackStack(R.id.armaduraFragment, true)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiError.collect {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun rellenarCamposDeArmadura() {
        with(binding) {
            etNombreArmadura.setText(armadura?.name)
            etCalidad.setText(armadura?.quality.toString())
            etDescripcion.setText(armadura?.description)
            etFIL.setText(armadura?.fil.toString())
            etCON.setText(armadura?.con.toString())
            etPEN.setText(armadura?.pen.toString())
            etCAL.setText(armadura?.cal.toString())
            etELE.setText(armadura?.ele.toString())
            etFRI.setText(armadura?.fri.toString())
            etENE.setText(armadura?.ene.toString())
            etArmadura.setText(armadura?.armor.toString())
            etValor.setText(armadura?.value.toString())
            etPeso.setText(armadura?.weight.toString())
        }
    }

    private fun FragmentArmaduraBinding.buildArmaduraActualizada(): Armadura? {
        armadura?.name = etNombreArmadura.text.toString()
        armadura?.quality = etCalidad.text.toString().toInt()
        armadura?.description = etDescripcion.text.toString()
        armadura?.fil = etFIL.text.toString().toInt()
        armadura?.con = etCON.text.toString().toInt()
        armadura?.pen = etPEN.text.toString().toInt()
        armadura?.cal = etCAL.text.toString().toInt()
        armadura?.ele = etELE.text.toString().toInt()
        armadura?.fri = etFRI.text.toString().toInt()
        armadura?.ene = etENE.text.toString().toInt()
        armadura?.armor = etArmadura.text.toString().toInt()
        armadura?.value = etValor.text.toString().toInt()
        armadura?.weight = etPeso.text.toString().toDouble()
        return armadura
    }

    private fun FragmentArmaduraBinding.faltaAlgunDato() =
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