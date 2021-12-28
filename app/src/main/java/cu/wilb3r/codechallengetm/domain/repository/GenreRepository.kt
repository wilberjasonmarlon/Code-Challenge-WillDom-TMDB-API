package cu.wilb3r.codechallengetm.domain.repository

import cu.wilb3r.codechallengetm.data.local.entities.DBGenre

interface GenreRepository {
    suspend fun getGenre(): List<DBGenre>
}