package ies.quevedo.chardat.framework.fragmentShowObjeto

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
import ies.quevedo.chardat.databinding.FragmentObjetoBinding
import ies.quevedo.chardat.domain.model.Objeto
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class ShowObjetoFragment : Fragment() {

    private val viewModel by viewModels<ShowObjetoViewModel>()
    private var _binding: FragmentObjetoBinding? = null
    private val binding get() = _binding!!
    private var idObjeto: Int? = null
    private var idPersonaje: Int? = null
    private var objeto: Objeto? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentObjetoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pedirObjeto()
        with(binding) {
            btCancelar.setOnClickListener {
                activity?.onBackPressed()
                findNavController().popBackStack(R.id.objetoFragment, true)
            }
            btModificar.setOnClickListener {
                if (faltaAlgunDato()) {
                    Toast.makeText(requireContext(), "Rellena todos los campos", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    val objetoActualizado = buildObjetoActualizado()
                    updateObjetoAndGoBack(objetoActualizado)
                }
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.clear()
    }

    private fun pedirObjeto() {
        viewModel.handleEvent(ShowObjetoContract.Event.FetchObjeto(idObjeto ?: 0))
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { value ->
                if (value.objeto != null) {
                    objeto = value.objeto
                    rellenarCamposDeObjeto()
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiError.collect {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updateObjetoAndGoBack(objetoActualizado: Objeto?) {
        viewModel.handleEvent(ShowObjetoContract.Event.PutObjeto(objetoActualizado))
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { value ->
                if (value.objetoActualizado != null) {
                    val action =
                        ShowObjetoFragmentDirections.actionObjetoFragmentToRVObjetoFragment(
                            idPersonaje ?: 0,
                        )
                    findNavController().navigate(action)
                    findNavController().popBackStack(R.id.objetoFragment, true)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiError.collect {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun rellenarCamposDeObjeto() {
        with(binding) {
            etNombreObjeto.setText(objeto?.name)
            etDescripcion.setText(objeto?.description)
            etValor.setText(objeto?.value.toString())
            etCantidad.setText(objeto?.amount.toString())
            etPeso.setText(objeto?.weight.toString())
        }
    }

    private fun FragmentObjetoBinding.buildObjetoActualizado(): Objeto? {
        objeto?.name = etNombreObjeto.text.toString().uppercase(Locale.getDefault())
        objeto?.description = etDescripcion.text.toString()
        objeto?.value = etValor.text.toString().toInt()
        objeto?.amount = etCantidad.text.toString().toInt()
        objeto?.weight = etPeso.text.toString().toDouble()
        return objeto
    }

    private fun FragmentObjetoBinding.faltaAlgunDato() =
        etNombreObjeto.text.isNullOrEmpty() ||
                etDescripcion.text.isNullOrEmpty() ||
                etValor.text.isNullOrEmpty() ||
                etCantidad.text.isNullOrEmpty() ||
                etPeso.text.isNullOrEmpty()
}