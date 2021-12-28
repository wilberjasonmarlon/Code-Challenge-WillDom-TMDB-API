package cu.wilb3r.codechallengetm.domain.repository

import androidx.paging.PagingData
import cu.wilb3r.codechallengetm.data.local.db.dao.TvAndTvPopular
import cu.wilb3r.codechallengetm.data.local.db.dao.TvAndTvTop
import cu.wilb3r.codechallengetm.data.local.entities.DBTv
import cu.wilb3r.codechallengetm.data.remote.model.CastResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface TvRepository {
    suspend fun getPopularTv(): Flow<PagingData<TvAndTvPopular>>
    suspend fun getTopRatedTv(): Flow<PagingData<TvAndTvTop>>
    //suspend fun getCast(video_id: Int): Response<CastResponse>
}