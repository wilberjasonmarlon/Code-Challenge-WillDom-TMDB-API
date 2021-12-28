package cu.wilb3r.codechallengetm.domain.repository

import androidx.paging.PagingData
import cu.wilb3r.codechallengetm.data.local.db.dao.TvAndTvPopular
import cu.wilb3r.codechallengetm.data.local.db.dao.TvAndTvTop
import kotlinx.coroutines.flow.Flow

interface TvRepository {
    suspend fun getPopularTv(): Flow<PagingData<TvAndTvPopular>>
    suspend fun getTopRatedTv(): Flow<PagingData<TvAndTvTop>>
    //suspend fun getCast(video_id: Int): Response<CastResponse>
}