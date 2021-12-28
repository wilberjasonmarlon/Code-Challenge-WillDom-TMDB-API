package cu.wilb3r.codechallengetm.data.local.mapper

import cu.wilb3r.codechallengetm.data.local.entities.DBTv
import cu.wilb3r.codechallengetm.data.remote.model.Tv
import cu.wilb3r.codechallengetm.domain.model.Media
import cu.wilb3r.codechallengetm.domain.model.MediaType

fun Tv.toEntity() =
    DBTv(
        backdrop_path,
        first_air_date,
        genre_ids,
        genre,
        id,
        name,
        origin_country,
        original_language,
        original_name,
        overview,
        popularity,
        poster_path,
        vote_average,
        vote_count
    )

fun DBTv.toMedia() = Media(id ?: 0, poster_path, backdrop_path, name, overview, vote_average, vote_count, MediaType.TV)