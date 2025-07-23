package ies.quevedo.chardat.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Escudo(
    val id: Int = 0,
    var name: String,
    var value: Int,
    var weight: Double,
    var quality: Int,
    var attackHability: Int,
    var damage: Int,
    var parry: Int,
    var description: String,
    val idPJ: Int
) : Parcelable