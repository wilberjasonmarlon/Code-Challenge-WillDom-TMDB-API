package cu.wilb3r.codechallengetm.domain.repository

import androidx.paging.PagingData
import cu.wilb3r.codechallengetm.data.local.db.dao.MovieAndMoviePopular
import cu.wilb3r.codechallengetm.data.local.db.dao.MovieAndMovieTop
import cu.wilb3r.codechallengetm.data.remote.model.CastResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface MovieRepository {
    suspend fun getPopularMovies(): Flow<PagingData<MovieAndMoviePopular>>
    suspend fun getTopRatedMovies(): Flow<PagingData<MovieAndMovieTop>>
    suspend fun getCast(video_id: Int): Response<CastResponse>

}