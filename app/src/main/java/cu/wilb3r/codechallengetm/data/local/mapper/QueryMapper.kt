package cu.wilb3r.codechallengetm.data.local.mapper

import cu.wilb3r.codechallengetm.data.remote.model.dell.SearchResult
import cu.wilb3r.codechallengetm.domain.model.Media
import cu.wilb3r.codechallengetm.domain.model.MediaType

fun SearchResult.toMedia() = Media(
    id,
    poster_path,
    backdrop_path,
    name,
    overview,
    vote_average,
    vote_count,
    type = media_type!!
)
