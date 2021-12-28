package cu.wilb3r.codechallengetm.data.repository.movie

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import cu.wilb3r.codechallengetm.data.local.db.dao.MovieAndMoviePopular
import cu.wilb3r.codechallengetm.data.local.db.dao.MovieAndMovieTop
import cu.wilb3r.codechallengetm.data.local.db.dao.MoviePopularDao
import cu.wilb3r.codechallengetm.data.local.db.dao.MovieTopDao
import cu.wilb3r.codechallengetm.data.local.entities.DBGenre
import cu.wilb3r.codechallengetm.data.local.mapper.toEntity
import cu.wilb3r.codechallengetm.data.remote.model.Cast
import cu.wilb3r.codechallengetm.data.remote.model.CastResponse
import cu.wilb3r.codechallengetm.data.remote.model.Resource
import cu.wilb3r.codechallengetm.data.repository.movie.datasource.PopularMovieRemoteMediator
import cu.wilb3r.codechallengetm.data.repository.movie.datasource.TopMovieRemoteMediator
import cu.wilb3r.codechallengetm.domain.datasource.CastDataSource
import cu.wilb3r.codechallengetm.domain.repository.MovieRepository
import cu.wilb3r.constant.Api.PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class MovieRepositoryImp
@ExperimentalPagingApi
@Inject constructor(
    private val moviePopularDao: MoviePopularDao,
    private val movieTopDao: MovieTopDao,
    private val popularRemoteMediator: PopularMovieRemoteMediator,
    private val topRemoteMediator: TopMovieRemoteMediator,
    private val castDataSource: CastDataSource
) : MovieRepository {

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getPopularMovies(): Flow<PagingData<MovieAndMoviePopular>> {
        return Pager(
            PagingConfig(pageSize = PAGE_SIZE, prefetchDistance = 2, enablePlaceholders = true),
            remoteMediator = popularRemoteMediator,
            pagingSourceFactory = { moviePopularDao.getPopularMovies() }
        ).flow
    }

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getTopRatedMovies(): Flow<PagingData<MovieAndMovieTop>> {
        return Pager(
            PagingConfig(pageSize = PAGE_SIZE, prefetchDistance = 2, enablePlaceholders = true),
            remoteMediator = topRemoteMediator,
            pagingSourceFactory = { movieTopDao.getTopMovies() }
        ).flow
    }

    override suspend fun getCast(video_id: Int) = castDataSource.getMovieCast(video_id)


}