package cu.wilb3r.codechallengetm.data.repository

import cu.wilb3r.codechallengetm.BuildConfig
import cu.wilb3r.codechallengetm.data.local.entities.DBGenre
import cu.wilb3r.codechallengetm.data.local.mapper.toEntity
import cu.wilb3r.codechallengetm.data.remote.TMDBApiService
import cu.wilb3r.codechallengetm.domain.repository.GenreRepository
import cu.wilb3r.constant.Api
import javax.inject.Inject

class GenreRepositoryImp @Inject constructor(
    private val api: TMDBApiService
) : GenreRepository {
    val param = HashMap<String, String>().apply {
        put(Api.API_KEY_STR, BuildConfig.API_KEY)
    }

    override suspend fun getGenre(): List<DBGenre> {
        val genreMovie = api.getMovieGenre(param).genres.map { it.toEntity() }
        val genreTv = api.getTvGenre(param).genres.map { it.toEntity() }

        return genreMovie.plus(genreTv)
    }

}