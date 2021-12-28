package cu.wilb3r.codechallengetm.data.repository.movie.datasource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import cu.wilb3r.codechallengetm.BuildConfig.API_KEY
import cu.wilb3r.codechallengetm.data.local.db.dao.GenreDao
import cu.wilb3r.codechallengetm.data.local.db.dao.MovieAndMoviePopular
import cu.wilb3r.codechallengetm.data.local.db.dao.MovieDao
import cu.wilb3r.codechallengetm.data.local.db.dao.MoviePopularDao
import cu.wilb3r.codechallengetm.data.local.entities.DBMovie
import cu.wilb3r.codechallengetm.data.local.entities.MoviePopular
import cu.wilb3r.codechallengetm.data.local.mapper.toEntity
import cu.wilb3r.codechallengetm.data.remote.TMDBApiService
import cu.wilb3r.constant.Api.API_KEY_STR
import cu.wilb3r.constant.Api.FIRST_PAGE
import cu.wilb3r.constant.Api.LAST_PAGE
import cu.wilb3r.constant.Api.PAGE
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@ExperimentalPagingApi
class PopularMovieRemoteMediator @Inject constructor(
    private val movieDao: MovieDao,
    private val genreDao: GenreDao,
    private val moviePopularDao: MoviePopularDao,
    private val api: TMDBApiService
) : RemoteMediator<Int, MovieAndMoviePopular>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieAndMoviePopular>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> FIRST_PAGE
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    getKeys() ?: FIRST_PAGE
                }
            }
            println(">> Page: $page")
            val param = HashMap<String, String>().apply {
                put(API_KEY_STR, API_KEY)
                put(PAGE, "$page")
            }
            api.getMoviePopular(param).body()?.results?.let { result ->
                val movies = ArrayList<DBMovie>()
                val moviesP = ArrayList<MoviePopular>()
                result.forEach {
                    val genres = ArrayList<String>()
                    it.toEntity().let { entity ->
                        entity.genre_ids?.forEach { id ->
                            genreDao.get(id).let { genre ->
                                genre.firstOrNull()?.name?.let { genreName ->
                                    genres.add(genreName).also {
                                        entity.genre = genres.toList()
                                    }
                                }
                            }
                        }
                        movies.add(entity)
                    }
                    moviesP.add(MoviePopular(it.id ?: 0, page))
                }
                movieDao.insertAll(movies)
                moviePopularDao.insertAll(moviesP)
            }
            MediatorResult.Success(endOfPaginationReached = page == LAST_PAGE)
        } catch (exception: IOException) {
            MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            MediatorResult.Error(exception)
        }
    }

    private suspend fun getKeys(): Int? {
        return moviePopularDao.getKeys().lastOrNull()?.page?.plus(1)
    }
}