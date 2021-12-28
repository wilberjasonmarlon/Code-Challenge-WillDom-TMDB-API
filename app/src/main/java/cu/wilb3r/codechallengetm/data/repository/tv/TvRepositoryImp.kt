package cu.wilb3r.codechallengetm.data.repository.tv

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import cu.wilb3r.codechallengetm.data.local.db.dao.*
import cu.wilb3r.codechallengetm.data.repository.tv.datasource.PopularTvRemoteMediator
import cu.wilb3r.codechallengetm.data.repository.tv.datasource.TopTvRemoteMediator
import cu.wilb3r.codechallengetm.domain.datasource.CastDataSource
import cu.wilb3r.codechallengetm.domain.repository.TvRepository
import cu.wilb3r.constant.Api.PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TvRepositoryImp
@ExperimentalPagingApi
@Inject constructor(
    private val tvPopularDao: TvPopularDao,
    private val tvTopDao: TvTopDao,
    private val popularRemoteMediator: PopularTvRemoteMediator,
    private val topRemoteMediator: TopTvRemoteMediator,
    private val castDataSource: CastDataSource
) : TvRepository {

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getPopularTv(): Flow<PagingData<TvAndTvPopular>> {
        return Pager(
            PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false, prefetchDistance = 3),
            remoteMediator = popularRemoteMediator,
            pagingSourceFactory = { tvPopularDao.getPopularTv() }
        ).flow
    }

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getTopRatedTv(): Flow<PagingData<TvAndTvTop>> {
        return Pager(
            PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false, prefetchDistance = 3),
            remoteMediator = topRemoteMediator,
            pagingSourceFactory = { tvTopDao.getTopTv() }
        ).flow
    }


}