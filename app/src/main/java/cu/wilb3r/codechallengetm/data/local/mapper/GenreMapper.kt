package cu.wilb3r.codechallengetm.data.local.mapper

import cu.wilb3r.codechallengetm.data.local.entities.DBGenre
import cu.wilb3r.codechallengetm.data.remote.model.Genre

fun Genre.toEntity() = DBGenre(id, "$name")

fun DBGenre.fromEntity() = Genre(id, name)