package cu.wilb3r.codechallengetm.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Media(
    val id: Int,
    val poster_path: String?,
    val backdrop_path: String?,
    val name: String?,
    val overview: String?,
    val vote_average: Double?,
    val vote_count: Int?,
    @MediaType val type: String
) : Parcelable
