package cu.wilb3r.codechallengetm.data.local.mapper

import cu.wilb3r.codechallengetm.data.local.entities.DBGenre
import cu.wilb3r.codechallengetm.data.local.entities.DBMovie
import cu.wilb3r.codechallengetm.data.remote.model.Genre
import cu.wilb3r.codechallengetm.data.remote.model.Movie

fun Genre.toEntity() = DBGenre( id,"$name")

fun DBGenre.fromEntity() = Genre(id, name)