package cu.wilb3r.codechallengetm.domain.usecases

import androidx.paging.PagingData
import cu.wilb3r.codechallengetm.data.local.db.dao.MovieAndMoviePopular
import cu.wilb3r.codechallengetm.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPopularMoviesUseCase @Inject constructor(private val repo: MovieRepository) {
    suspend fun invoke(): Flow<PagingData<MovieAndMoviePopular>> = repo.getPopularMovies()
}