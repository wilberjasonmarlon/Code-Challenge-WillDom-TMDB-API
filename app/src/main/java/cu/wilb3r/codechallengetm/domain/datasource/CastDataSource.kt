package cu.wilb3r.codechallengetm.domain.datasource

import cu.wilb3r.codechallengetm.data.remote.model.CastResponse
import retrofit2.Response

interface CastDataSource {
    suspend fun getMovieCast(video_id: Int): Response<CastResponse>
    suspend fun getTvCast(video_id: Int): Response<CastResponse>
}