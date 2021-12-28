package cu.wilb3r.codechallengetm.data.repository.tv.datasource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import cu.wilb3r.codechallengetm.BuildConfig.API_KEY
import cu.wilb3r.codechallengetm.data.local.db.dao.GenreDao
import cu.wilb3r.codechallengetm.data.local.db.dao.TvAndTvPopular
import cu.wilb3r.codechallengetm.data.local.db.dao.TvDao
import cu.wilb3r.codechallengetm.data.local.db.dao.TvPopularDao
import cu.wilb3r.codechallengetm.data.local.entities.DBTv
import cu.wilb3r.codechallengetm.data.local.entities.TvPopular
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
class PopularTvRemoteMediator @Inject constructor(
    private val tvDao: TvDao,
    private val genreDao: GenreDao,
    private val tvPopularDao: TvPopularDao,
    private val api: TMDBApiService
) : RemoteMediator<Int, TvAndTvPopular>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, TvAndTvPopular>
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
            api.getTvPopular(param).body()?.results?.let { result ->
                val tvs = ArrayList<DBTv>()
                val tvsP = ArrayList<TvPopular>()
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
                        tvs.add(entity)
                    }
                    tvsP.add(TvPopular(it.id ?: 0, page))
                }
                tvDao.insertAll(tvs)
                tvPopularDao.insertAll(tvsP)
            }
            MediatorResult.Success(endOfPaginationReached = page == LAST_PAGE)
        } catch (exception: IOException) {
            MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            MediatorResult.Error(exception)
        }
    }

    private suspend fun getKeys(): Int? {
        return tvPopularDao.getKeys().lastOrNull()?.page?.plus(1)
    }
}