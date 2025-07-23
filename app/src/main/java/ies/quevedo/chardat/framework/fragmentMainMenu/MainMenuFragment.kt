package ies.quevedo.chardat.framework.fragmentMainMenu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ies.quevedo.chardat.R
import ies.quevedo.chardat.databinding.FragmentMainMenuBinding
import ies.quevedo.chardat.domain.model.Personaje
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainMenuFragment : Fragment() {

    private val viewModel by viewModels<MainMenuViewModel>()
    private var _binding: FragmentMainMenuBinding? = null
    private val binding get() = _binding!!
    private var personaje: Personaje? = null
    private var idPersonaje: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentMainMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        idPersonaje = arguments?.getInt("idPersonaje") ?: 0
        viewModel.handleEvent(MainMenuContract.Event.FetchPersonaje(idPersonaje ?: 0))
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { value ->
                binding.loading.visibility = if (value.isLoading) View.VISIBLE else View.GONE
                personaje = value.personaje
                rellenarDatosDelPersonaje()
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiError.collect {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
        setListenerActions()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.clear()
    }

    private fun setListenerActions() {
        with(binding) {
            var action: NavDirections
            ivInfo.setOnClickListener {
                action =
                    MainMenuFragmentDirections.actionMainMenuFragmentToPersonajeFragment(
                        idPersonaje ?: 0
                    )
                findNavController().navigate(action)
            }
            ivArmas.setOnClickListener {
                action =
                    MainMenuFragmentDirections.actionMainMenuFragmentToRVArmaFragment(
                        idPersonaje ?: 0
                    )
                findNavController().navigate(action)
            }
            ivArmaduras.setOnClickListener {
                action = MainMenuFragmentDirections.actionMainMenuFragmentToRVArmaduraFragment(
                    idPersonaje ?: 0
                )
                findNavController().navigate(action)
            }
            ivEscudos.setOnClickListener {
                action =
                    MainMenuFragmentDirections.actionMainMenuFragmentToRVEscudoFragment(
                        idPersonaje ?: 0
                    )
                findNavController().navigate(action)
            }
            ivObjetos.setOnClickListener {
                action =
                    MainMenuFragmentDirections.actionMainMenuFragmentToRVObjetoFragment(
                        idPersonaje ?: 0
                    )
                findNavController().navigate(action)
            }
        }
    }

    private fun rellenarDatosDelPersonaje() {
        with(binding) {
            setImageClass(personaje)
            tvName.text = personaje?.name
            tvClase.text = personaje?.clase
        }
    }

    private fun setImageClass(personaje: Personaje?) {
        when (personaje?.clase) {
            "GUERRERO" -> binding.ivClaseBanner.setImageResource(R.drawable.guerrero_banner)
            "GUERRERO ACRÓBATA" -> binding.ivClaseBanner.setImageResource(R.drawable.guerrero_acrobata_banner)
            "PALADÍN" -> binding.ivClaseBanner.setImageResource(R.drawable.paladin_banner)
            "PALADÍN OSCURO" -> binding.ivClaseBanner.setImageResource(R.drawable.paladin_oscuro_banner)
            "MAESTRO DE ARMAS" -> binding.ivClaseBanner.setImageResource(R.drawable.maestro_de_armas_banner)
            "TECNICISTA" -> binding.ivClaseBanner.setImageResource(R.drawable.tecnicista_banner)
            "TAO" -> binding.ivClaseBanner.setImageResource(R.drawable.tao_banner)
            "EXPLORADOR" -> binding.ivClaseBanner.setImageResource(R.drawable.explorador_banner)
            "SOMBRA" -> binding.ivClaseBanner.setImageResource(R.drawable.sombra_banner)
            "LADRÓN" -> binding.ivClaseBanner.setImageResource(R.drawable.ladron_banner)
            "ASESINO" -> binding.ivClaseBanner.setImageResource(R.drawable.asesino_banner)
            "HECHICERO" -> binding.ivClaseBanner.setImageResource(R.drawable.hechicero_banner)
            "WARLOCK" -> binding.ivClaseBanner.setImageResource(R.drawable.warlock_banner)
            "ILUSIONISTA" -> binding.ivClaseBanner.setImageResource(R.drawable.ilusionista_banner)
            "HECHICERO MENTALISTA" -> binding.ivClaseBanner.setImageResource(R.drawable.hechicero_mentalista_banner)
            "CONJURADOR" -> binding.ivClaseBanner.setImageResource(R.drawable.conjurador_banner)
            "GUERRERO CONJURADOR" -> binding.ivClaseBanner.setImageResource(R.drawable.guerrero_conjurador_banner)
            "MENTALISTA" -> binding.ivClaseBanner.setImageResource(R.drawable.mentalista_banner)
            "GUERRERO MENTALISTA" -> binding.ivClaseBanner.setImageResource(R.drawable.guerrero_mentalista_banner)
            "NOVEL" -> binding.ivClaseBanner.setImageResource(R.drawable.novel_banner)
        }
    }
}